package snowy.util

import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object FutureAwaiting {

  implicit class FutureAwait[A](future: Future[A]) {
    def await(timeout: FiniteDuration = 3 seconds): A = {
      Await.result(future, timeout)
    }
  }

}
