package snowy.util

import java.io.File
import java.net.{URL, URLDecoder}
import java.nio.file.{Files, Path, Paths}
import java.util.jar.JarFile
import scala.collection.JavaConverters._

case class ResourceNotFound(msg: String) extends RuntimeException(msg)

/** utilities for working with resources on the classpath */
object ResourceUtil {

  /** return a list of the resources within a resource folder
    * (works whether the resource is mapped to the file system or to .jar file
    */
  def byDirectory(resourcePath: String,
                  classLoader: ClassLoader =
                    Thread.currentThread().getContextClassLoader()): Iterable[String] = {
    val url = Option(classLoader.getResource(resourcePath)).getOrElse {
      throw new ResourceNotFound(resourcePath)
    }

    url.getProtocol match {
      case "file" => childrenFromFile(url)
      case "jar"  => childrenFromJar(url, resourcePath)
    }
  }

  /** (for testing) return a fileystem path to a given resource. Only works
    *  on resources in the filesystem (jar resources will throw an exception) */
  def filePath(resourcePath: String): Path = {
    val possibleResource =
      Thread.currentThread.getContextClassLoader.getResource(resourcePath)
    val resource = Option(possibleResource).getOrElse {
      throw new ResourceNotFound(resourcePath)
    }

    Paths.get(resource.toURI)
  }

  /** load children of a path from a file resource */
  protected def childrenFromFile(fileUrl: URL): Iterable[String] = {
    val file = new File(fileUrl.toURI())
    file.list()
  }

  /** return children of a path from the .jar file. Since the .jar file records full paths of all
    * files, we need to scan the jar table of contents to extract the child relationships for the
    * path.
    */
  protected[util] def childrenFromJar(jarUrl: URL,
                                      resourcePath: String): Iterable[String] = {
    object Child {
      val TakeToSlash = """([^/]*)""".r

      /** given a full path, extract the children (not including the grandchildren) of the
        * resourcePath folder.
        *
        * e.g. given foo/bar/bah/bee and a resourcePath of foo/bar, extract bah
        */
      def unapply(path: String): Option[String] = {
        if (path.startsWith(resourcePath)) {
          val suffix  = path.stripPrefix(resourcePath + "/")
          val toSlash = TakeToSlash.findFirstIn(suffix).get
          Some(toSlash)
        } else {
          None
        }
      }
    }

    // url is e.g. "file:/home/me/foo/bah.jar!/resourcePath
    // we want: /home/me/foo/bah.jar
    val pathToJar   = jarUrl.getPath.stripPrefix("file:").stripSuffix(s"!/$resourcePath")
    val decodedPath = URLDecoder.decode(pathToJar, "UTF-8")
    val jar         = new JarFile(decodedPath)
    val children = jar.entries().asScala.map(_.getName).collect {
      case Child(child) => child
    }
    children.toSet
  }

}
