package snowy.client.hud

import org.scalajs.dom._

class DeathMessage {
  private val notifications =
    document.getElementById("notifications").asInstanceOf[html.Div]
  private var timeout = 0

  private def createHtml(killed: String): String = {
    s"<div class='message message-top'><b>You iced <div class='kill'>$killed</div></b></div>"
  }

  def killedSled(killed: String): Unit = {
    window.clearInterval(timeout)
    notifications.innerHTML = createHtml(killed)
    timeout = window.setTimeout(
      () => {
        notifications.innerHTML = ""
      },
      4500
    )
  }
}
