package info.gianlucacosta.maanbloem.moondeploy.descriptors

import java.io.{File, FileFilter, IOException}
import java.nio.file.{Files, Path}

import scala.collection.mutable


object DescriptorFinder {
  def findDescriptors(galleryDirectory: Path): List[Path] = {
    if (!Files.isDirectory(galleryDirectory)) {
      return List()
    }


    val descriptors = mutable.MutableList[Path]()

    visitDirectory(galleryDirectory.toFile, descriptors)

    descriptors.toList
  }


  private def visitDirectory(directory: File, cumulatedDescriptors: mutable.MutableList[Path]): Unit = {
    val regularFiles = directory.listFiles(new FileFilter {
      override def accept(file: File): Boolean =
        file.isFile
    })

    if (regularFiles == null) {
      throw new IOException(s"Could not list the files in ${directory}")
    }

    val appDescriptor =
      regularFiles
        .find(file => file.getName.endsWith(".moondeploy"))


    if (appDescriptor.isDefined) {
      cumulatedDescriptors += appDescriptor.get.toPath
      return
    }


    val subDirectories = directory.listFiles(new FileFilter {
      override def accept(file: File): Boolean =
        file.isDirectory
    })

    if (subDirectories == null) {
      throw new IOException(s"Could not list the directories in ${directory}")
    }

    subDirectories.foreach(subDirectory => {
      visitDirectory(subDirectory, cumulatedDescriptors)
    })
  }
}
