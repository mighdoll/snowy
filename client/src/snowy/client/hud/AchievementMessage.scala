package snowy.client.hud

import org.scalajs.dom._
import snowy.GameClientProtocol.{AchievementBonus, HealthBonus, ScoreBonus, SpeedBonus}

class AchievementMessage {
  private val achievements =
    document.getElementById("achievements").asInstanceOf[html.Div]
  private var timeout = 0

  private def createHtml(title: String, desc: String, color: String): String = {
    s"<div class='message message-bottom'><b class='$color'>$title</b><hr><p class='description'>$desc</p></div>"
  }

  def display(bonus: AchievementBonus, title: String, desc: String): Unit = {
    window.clearInterval(timeout)
    achievements.innerHTML = bonus match {
      case SpeedBonus  => createHtml(title, desc, "yellow")
      case HealthBonus => createHtml(title, desc, "yellow")
      case ScoreBonus  => createHtml(title, desc, "")
    }
    timeout = window.setTimeout(
      () => {
        achievements.innerHTML = ""
      },
      4500
    )
  }
}
