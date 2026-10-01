package info.gianlucacosta.maanbloem.moondeploy

import java.io.{File, FileReader}
import java.nio.file.Paths
import javax.json.Json

import info.gianlucacosta.helios.desktop.DesktopUtils

object MoonSettings {
  lazy val localDirectory = {
    val userDirectory = DesktopUtils.homeDirectory

    if (userDirectory.isEmpty) {
      throw new RuntimeException("Cannot find the user directory")
    }

    val defaultLocalDirectory = new File(userDirectory.get, "MoonDeploy")

    val settingsFile = new File(userDirectory.get, ".moondeploy.json")

    if (settingsFile.isFile) {
      val settingsReader = Json.createReader(new FileReader(settingsFile))

      try {
        val settings = settingsReader.readObject()

        Paths.get(
          settings.getString("LocalDirectory", defaultLocalDirectory.getAbsolutePath)
        )
      } catch {
        case ex: Exception =>
          defaultLocalDirectory.toPath
      } finally {
        settingsReader.close()
      }

    } else {
      defaultLocalDirectory.toPath
    }
  }

  lazy val appGalleryDirectory = localDirectory.resolve("apps")
}
