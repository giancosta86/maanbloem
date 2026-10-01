package info.gianlucacosta.maanbloem.ui

import java.io.File
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.collections.ObservableList

import info.gianlucacosta.maanbloem.App
import info.gianlucacosta.maanbloem.moondeploy.descriptors.Descriptor
import net.sf.image4j.codec.ico.ICODecoder

import scalafx.Includes._
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{Label, TableCell, TableColumn, TableView}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.Priority


class DescriptorTableView(fxDescriptors: ObservableList[Descriptor]) extends TableView[Descriptor](fxDescriptors) {
  private val IconSize = 64
  private val CellPadding = 10
  private val DefaultIconImage = new Image(App.getResourceAsStream("moondeploy.png"), IconSize, IconSize, true, true)

  hgrow = Priority.Always

  placeholder = new Label("No apps installed")

  columns ++= Seq(
    new TableColumn[Descriptor, Option[File]] {
      sortable = false

      cellValueFactory = { row =>
        new ReadOnlyObjectWrapper[Option[File]](row.value.iconFile)
      }

      cellFactory = { _: TableColumn[Descriptor, Option[File]] =>
        new TableCell[Descriptor, Option[File]] {
          item.onChange { (_, _, newIconFileOption) =>
            if (newIconFileOption != null) {
              val iconImage = getIconImage(newIconFileOption)

              graphic = new ImageView(iconImage)

              padding = Insets(CellPadding)
            }
          }
        }
      }
    },

    new TableColumn[Descriptor, String] {
      text = "Name"
      sortable = true
      prefWidth = 250

      cellValueFactory = { row => new ReadOnlyObjectWrapper[String](row.value.name) }

      cellFactory = createLabelCellFactory()
    },

    new TableColumn[Descriptor, String] {
      text = "Version"
      sortable = true
      prefWidth = 80

      cellValueFactory = { row => new ReadOnlyObjectWrapper[String](row.value.appVersion) }

      cellFactory = createLabelCellFactory()
    },


    new TableColumn[Descriptor, String] {
      text = "Publisher"
      sortable = true
      prefWidth = 250

      cellValueFactory = { row => new ReadOnlyObjectWrapper[String](row.value.publisher) }

      cellFactory = createLabelCellFactory()
    },

    new TableColumn[Descriptor, String] {
      text = "Address"
      sortable = true
      prefWidth = 250

      cellValueFactory = { row => new ReadOnlyObjectWrapper[String](row.value.baseUrl.toString) }

      cellFactory = createLabelCellFactory()
    }
  )


  private def getIconImage(iconFileOption: Option[File]): Image = {
    if (iconFileOption.isEmpty) {
      return DefaultIconImage
    }

    val iconFile = iconFileOption.get

    if (iconFile.getName.endsWith(".ico")) {
      try {
        val iconComponents = ICODecoder.read(iconFile)
        if (iconComponents == null || iconComponents.isEmpty) {
          return DefaultIconImage
        }

        val mainComponent = iconComponents.get(0)
        SwingFXUtils.toFXImage(mainComponent, null)
      } catch {
        case _: Exception =>
          DefaultIconImage
      }
    } else {
      val iconUrl = "file:" + iconFile.getAbsolutePath

      new Image(iconUrl, IconSize, IconSize, true, true)
    }
  }


  private def createLabelCellFactory() = { column: TableColumn[Descriptor, String] =>
    new TableCell[Descriptor, String] {
      item.onChange { (_, _, newValue) =>
        if (newValue != null) {
          graphic = new Label(newValue) {
            alignment = Pos.Center
            prefWidth <== column.width
            prefHeight = IconSize + 2 * CellPadding
            wrapText = true
            padding = Insets(CellPadding)
          }
        }
      }
    }
  }
}
