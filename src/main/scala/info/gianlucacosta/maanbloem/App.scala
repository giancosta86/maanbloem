package info.gianlucacosta.maanbloem


import javafx.stage.Stage

import info.gianlucacosta.helios.apps.{AppInfo, AuroraAppInfo}
import info.gianlucacosta.helios.fx.apps.{AppBase, AppMain, SplashStage}
import info.gianlucacosta.maanbloem.icons.MainIcon
import info.gianlucacosta.maanbloem.moondeploy.MoonSettings
import info.gianlucacosta.maanbloem.moondeploy.descriptors.{DescriptorFinder, DescriptorParser}
import info.gianlucacosta.maanbloem.ui.MainScene

import scalafx.application.Platform


object App extends AppMain[App](classOf[App])

class App extends AppBase(AuroraAppInfo(ArtifactInfo, MainIcon)) {
  override def startup(appInfo: AppInfo, splashStage: SplashStage, primaryStage: Stage): Unit = {
    splashStage.statusText = "Analyzing app descriptors..."

    val appGalleryPath = MoonSettings.appGalleryDirectory

    val descriptors =
      DescriptorFinder
        .findDescriptors(appGalleryPath)
        .map(DescriptorParser.parse)
        .filter(_.isDefined)
        .map(_.get)

    var mainScene: MainScene = null


    splashStage.statusText = "Initializing the window..."

    Platform.runLater {
      mainScene = new MainScene(appInfo, descriptors)
    }

    Platform.runLater {
      primaryStage.setScene(mainScene)
    }

    Platform.runLater {
      primaryStage.setWidth(1200)
      primaryStage.setHeight(700)
      primaryStage.centerOnScreen()
    }
  }
}
