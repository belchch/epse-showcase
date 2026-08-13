package dev.epse.app.boq.estimate.docx

import org.apache.poi.xwpf.usermodel.*
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth
import dev.epse.app.boq.estimate.EstimateReport
import dev.epse.app.boq.estimate.EstimateReportWork
import java.math.BigDecimal
import java.math.BigInteger

class EstimateReportDocxBuilder(
    private val estimateReport: EstimateReport
) {

    private val pageWidth = 16838L
    private val pageHeight = 11906L


    private val columnWidths = listOf(
        600L,   // № п/п (0)
        2000L,  // Наименование (1)
        600L,  // Ед. изм. (2)
        1000L,  // Объем (3)
        7000L,  // Поставщик услуг (4) - urlColumnWidth
        1000L,  // Цена услуги (5)
        1000L,  // Средняя услуги (6)
        1000L   // Средняя рыночная стоимость (7)
    )

    fun build(): XWPFDocument {
        val doc = XWPFDocument()
        fill(doc)
        return doc
    }

    fun fill(doc: XWPFDocument): XWPFDocument {
        landscapeOrientation(doc)

        val rowCount = estimateReport.groups.size * 2 +
        estimateReport.groups.flatMap { group -> group.works.flatMap { it.rates } }.size +
        7

        val table = doc.createTable(rowCount, 8)
        table.setWidth("100%")

        table.rows.forEach { row ->
            row.height = 0
            row.heightRule = TableRowHeightRule.AUTO
        }

        setupTableLayout(table, columnWidths)

        var rowIndex = 2
        var rowNum = 1

        tableDescriptionRow(table)
        tableHeaderRow(table)

        for (group in estimateReport.groups) {
            groupTitleRow(table, rowIndex++, group.description)

            for (work in group.works) {
                workRow(table, rowIndex, rowNum++, work)
                rowIndex += work.rates.count()
            }

            groupSummaryRow(table, rowIndex++, group.total)
        }

        tableSummaryRows(table, rowCount - 5, estimateReport)

        return doc
    }

    private fun landscapeOrientation(doc: XWPFDocument) {
        val sectPr: CTSectPr = doc.document.body.addNewSectPr()
        val pageSize = sectPr.addNewPgSz()
        pageSize.orient = STPageOrientation.LANDSCAPE
        pageSize.w = BigInteger.valueOf(pageWidth)
        pageSize.h = BigInteger.valueOf(pageHeight)
    }
}

fun setupTableLayout(table: XWPFTable, columnWidths: List<Long>) {
    // 1. Устанавливаем фиксированный layout
    val tblPr = table.ctTbl.tblPr ?: table.ctTbl.addNewTblPr()
    val tblLayout = tblPr.tblLayout ?: tblPr.addNewTblLayout()
    tblLayout.type = STTblLayoutType.FIXED

    // 2. Устанавливаем общую ширину таблицы
    val tblWidth = tblPr.tblW ?: tblPr.addNewTblW()
    tblWidth.type = STTblWidth.DXA
    tblWidth.w = BigInteger.valueOf(columnWidths.sum())

    // 3. Настраиваем сетку колонок
    setupTableGrid(table, columnWidths)
}

private fun setupTableGrid(table: XWPFTable, columnWidths: List<Long>) {
    val grid = table.ctTbl.tblGrid ?: table.ctTbl.addNewTblGrid()
    grid.gridColList.clear()

    columnWidths.forEach { width ->
        val gridCol = grid.addNewGridCol()
        gridCol.w = BigInteger.valueOf(width)
    }
}

fun tableDescriptionRow(table: XWPFTable) {
    val row = table.getRow(0)
    row.insertHeaderText(0, listOf("СМЕТА", " на выполнение строительно-ремонтных работ"))
    row.insertHeaderText(5, listOf(""))
    row.insertHeaderText(6, listOf(""))
    row.insertHeaderText(7, listOf(""))
    mergeCellsHorizontally(row, 0, 4)
}

fun tableHeaderRow(table: XWPFTable) {
    val row = table.getRow(1)

    row.insertHeaderText(0, listOf("№ п/п"))
    row.insertHeaderText(1, listOf("Наименование"))
    row.insertHeaderText(2, listOf("Ед.", "Изм."))
    row.insertHeaderText(3, listOf("Объем"))
    row.insertHeaderText(4, listOf("Поставщик услуг"))
    row.insertHeaderText(5, listOf("Цена", "услуги",  "(руб.)"))
    row.insertHeaderText(6, listOf("Средняя", "услуги",  "(руб.)"))
    row.insertHeaderText(7, listOf("Средняя", "рыночная", "стоимость", "услуги", "(руб.)"))
}

fun groupTitleRow(table: XWPFTable, rowIndex: Int, title: String) {
    val row = table.getRow(rowIndex)

    row.getCell(0).insertText("") { cell, run, paragraph ->
        cell.color = grey
    }

    row.getCell(1).insertText(title) { cell, run, paragraph ->
        run.isBold = true
        paragraph.alignment = ParagraphAlignment.CENTER
        cell.verticalAlignment = XWPFTableCell.XWPFVertAlign.CENTER
        paragraph.spacingBefore = 100
        paragraph.spacingAfter = 100
        cell.color = grey
    }

    mergeCellsHorizontally(row, 1, 6)
}

fun tableSummaryRows(table: XWPFTable, startRowIndex: Int, estimateReport: EstimateReport) {
    tableSummaryRow(
        table.getRow(startRowIndex),
        "Итого, руб,",
        estimateReport.worksTotal,
        grey
    )

    tableSummaryRow(
        table.getRow(startRowIndex + 1),
        "Непредвиденные расходы (2 % от итоговой суммы), руб.",
        estimateReport.unexpectedExpenses,
        greyLight
    )

    tableSummaryRow(
        table.getRow(startRowIndex + 2),
        "Вывоз мусора , руб.",
        estimateReport.garbageRemoval,
        greyLight
    )

    tableSummaryRow(
        table.getRow(startRowIndex + 3),
        "Транспортные расходы (5% от итоговой суммы), руб.",
        estimateReport.transportation,
        greyLight
    )

    tableSummaryRow(
        table.getRow(startRowIndex + 4),
        "Итого по всем разделам с непредвиденными расходами, руб.:",
        estimateReport.total,
        grey
    )
}

fun tableSummaryRow(row: XWPFTableRow, description: String, amount: BigDecimal, color: String) {
    row.getCell(0).insertText(description) { cell, run, paragraph ->
        cell.color = color
        run.isBold = true
    }

    row.getCell(5).insertText("") { cell, run, paragraph ->
        cell.color = color
    }

    row.getCell(6).insertText("") { cell, run, paragraph ->
        cell.color = color
    }

    row.getCell(7).insertText(amount.toString()) { cell, run, paragraph ->
        run.isBold = true
        paragraph.alignment = ParagraphAlignment.RIGHT
    }

    mergeCellsHorizontally(row, 0, 4)
}

fun groupSummaryRow(table: XWPFTable, rowIndex: Int, summary: BigDecimal) {
    val row = table.getRow(rowIndex)

    row.getCell(1).insertText("Итого по разделу, руб.") { cell, run, paragraph ->
        run.isBold = true
    }

    row.getCell(7).insertText(summary.toString()) { cell, run, paragraph ->
        run.isBold = true
        paragraph.alignment = ParagraphAlignment.RIGHT
    }

    emptyCells(row, 0, 5, 6)

    mergeCellsHorizontally(row, 1, 4)
}

fun workRow(table: XWPFTable, rowIndex: Int, rowNum: Int, data: EstimateReportWork) {
    val row = table.getRow(rowIndex)

    fun XWPFTableRow.insertTextRight(pos: Int, text: String) {
        getCell(pos).insertText(text) { cell, run, paragraph ->
            paragraph.alignment = ParagraphAlignment.RIGHT
        }
    }

    fun XWPFTableRow.insertText(pos: Int, text: String) {
        getCell(pos).insertText(text)
    }

    fun XWPFTableRow.insertTextCenter(pos: Int, text: String) {
        getCell(pos).insertText(text) { cell, run, paragraph ->
            paragraph.alignment = ParagraphAlignment.CENTER
        }
    }

    fun XWPFTableRow.insertUrlText(pos: Int, text: String) {
        getCell(pos).insertText(text) { cell, run, paragraph ->
            cell.color = greyLight
            run.color = "0563C1"
            run.setUnderline(UnderlinePatterns.SINGLE)
        }
    }

    row.insertTextCenter(0, (rowNum).toString())
    row.insertText(1, data.name)
    row.insertTextCenter(2, data.uom)
    row.insertTextCenter(3, data.volume.toString())

    if (data.rates.isNotEmpty()) {
        row.insertUrlText(4, data.rates.first().url)
        row.insertTextRight(5, data.rates.first().price.toString())
    }

    row.insertTextCenter(6, data.averagePrice.toString())
    row.insertTextRight(7, data.averageCost.toString())

    for (r in 1 until data.rates.size) {
        val rateRow = table.getRow(rowIndex + r)
        val rate = data.rates[r]

        rateRow.insertUrlText(4, rate.url)
        rateRow.insertTextRight(5, rate.price.toString())
    }

    mergeCellsVertically(table, rowIndex, rowIndex + data.rates.size - 1, 0 .. 3, 6 .. 7)

}

private fun mergeCellsHorizontally(row: XWPFTableRow, startCol: Int, endCol: Int) {
    row.getCell(startCol).ctTc.addNewTcPr().addNewHMerge().setVal(
        STMerge.RESTART
    )
    for (i in startCol + 1..endCol) {
        row.getCell(i).ctTc.addNewTcPr().addNewHMerge().setVal(
            STMerge.CONTINUE
        )
    }
}

private fun mergeCellsVertically(table: XWPFTable, fromRow: Int, toRow: Int, vararg ranges: IntRange) {
    for (range in ranges) {
        for (i in range) {
            mergeCellsVertically(table, i, fromRow, toRow)
        }
    }
}

private fun mergeCellsVertically(table: XWPFTable, col: Int, fromRow: Int, toRow: Int) {
    for (rowIndex in fromRow..toRow) {
        val cell = table.getRow(rowIndex).getCell(col)
        if (rowIndex == fromRow) {
            cell.ctTc.addNewTcPr().addNewVMerge().setVal(STMerge.RESTART)
        } else {
            cell.ctTc.addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE)
        }
    }
}

private fun emptyCells(row: XWPFTableRow, vararg cells: Int) {
    for (i in cells) {
        row.getCell(i).insertText("")
    }
}

fun XWPFTableRow.insertHeaderText(pos: Int, texts: List<String>) {
    getCell(pos).insertTextLines(texts) { cell, run, paragraph ->
        paragraph.alignment = ParagraphAlignment.CENTER
        cell.verticalAlignment = XWPFTableCell.XWPFVertAlign.CENTER
        run.isBold = true
        cell.color = greyLight
        paragraph.spacingBefore = 100
        paragraph.spacingAfter = 100
    }
}

private fun XWPFTableCell.insertText(text: String, format: ((cell: XWPFTableCell, run: XWPFRun, paragraph: XWPFParagraph) -> Unit)? = null) {
    insertTextLines(listOf(text), format)
}

private fun XWPFTableCell.insertTextLines(texts: List<String>, format: ((cell: XWPFTableCell, run: XWPFRun, paragraph: XWPFParagraph) -> Unit)? = null) {
    val paragraph = addParagraph()

    if (texts.isNotEmpty() && texts.first().isNotEmpty()) {
        paragraph.setIndentationLeft(142)
        paragraph.setIndentationRight(142)
        paragraph.spacingBefore = 30
        paragraph.spacingAfter = 30
    } else {
        paragraph.spacingBefore = 0
        paragraph.spacingAfter = 0
    }

    val run = paragraph.createRun()

    for ((index, text) in texts.withIndex()) {
        run.setText(text)

        if (index != texts.size - 1) {
            run.addCarriageReturn()
        }
    }

    run.fontFamily = "Times New Roman"
    run.fontSize = 8
    format?.let { format(this, run, paragraph) }
    removeParagraph(0)
}

private val grey = "D3D3D3"
private val greyLight = "E0E0E0"