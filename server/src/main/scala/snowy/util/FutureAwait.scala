package snowy.util

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object FutureAwaiting {

  implicit class FutureAwait[A](future: Future[A]) {
    def await(timeout: FiniteDuration = 3 seconds): A = {
      Await.result(future, timeout)
    }
  }

}
