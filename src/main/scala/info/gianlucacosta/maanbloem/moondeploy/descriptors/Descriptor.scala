package info.gianlucacosta.maanbloem.moondeploy.descriptors

import java.io.File
import java.net.URL
import java.nio.file.Path

case class Descriptor(
                       descriptorPath: Path,
                       name: String,
                       appVersion: String,
                       publisher: String,
                       description: String,
                       baseUrl: URL,
                       iconFile: Option[File]) {

  require(descriptorPath != null)
  require(name != null && !name.isEmpty)
  require(appVersion != null && !appVersion.isEmpty)
  require(publisher != null && !publisher.isEmpty)
  require(description != null && !description.isEmpty)
  require(baseUrl != null)

  lazy val appDirectory = descriptorPath.getParent
}
