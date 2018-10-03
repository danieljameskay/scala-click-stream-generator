package config

import com.typesafe.config.ConfigFactory

object  Settings {
  private val config = ConfigFactory.load()
  object WebLogGen {
    private val webLogGen = config.getConfig("clickstream")
    lazy val records: Int = webLogGen.getInt("records")
    lazy val timeMultipler: Int = webLogGen.getInt("time_multipler")
    lazy val pages: Int = webLogGen.getInt("pages")
    lazy val visitors: Int = webLogGen.getInt("visitors")
    lazy val filePath: String = webLogGen.getString("filePath")
  }
}
