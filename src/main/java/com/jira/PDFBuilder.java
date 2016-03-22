package com.jira;

import java.io.FileOutputStream;
import java.util.Collection;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFBuilder {

	public static void createPDF(String pdfFilename, Collection<Issue> issues) {
		Document doc = new Document();
		PdfWriter docWriter = null;
		try {
			// file path
			String path = pdfFilename;
			docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));

			// document header attributes
			doc.addAuthor("Slacker");
			doc.addCreationDate();
			doc.addTitle("Sprint Issues for Printing");
			doc.setPageSize(PageSize.LETTER);

			doc.open();

			for (Issue issue : issues) {
				doc.add(anIssueTable(issue));
			}

		} catch (DocumentException dex) {
			dex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (doc != null) {
				// close the document
				doc.close();
			}
			if (docWriter != null) {
				// close the writer
				docWriter.close();
			}
		}
	}

	private static Paragraph anIssueTable(Issue issue) {
		// special font sizes
		Font headingFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
		Font contentFont = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL, new BaseColor(0, 0, 0));

		// Two Columns
		float[] columnWidths = { 5f, 5f };
		Paragraph paragraph = new Paragraph();
		// create PDF table with the given widths
		PdfPTable table = new PdfPTable(columnWidths);
		// set table width a percentage of the page width
		table.setWidthPercentage(60f);
		// insert column headings
		insertCell(table, "Story No:" + issue.getKey(), Element.ALIGN_LEFT, 1, headingFont, false);
		insertCell(table, "Story Points:" + issue.getStoryPoints(), Element.ALIGN_RIGHT, 1, headingFont, false);
		table.setHeaderRows(1);
		table.setKeepTogether(true);
		table.setSplitRows(false);

		insertCell(table, "Summary", Element.ALIGN_LEFT, 2, headingFont, false);
		insertCell(table, issue.getSummary(), Element.ALIGN_LEFT, 2, contentFont, true);

		insertCell(table, "Acceptance Criteria", Element.ALIGN_LEFT, 2, headingFont, false);
		String acs = issue.getAcs();
		insertCell(table, acs == null ? " " : acs, Element.ALIGN_LEFT, 2, contentFont, true);

		insertCell(table, "----------------------------- cut here ----------------------------- ", Element.ALIGN_CENTER,
				2, contentFont, false);
		paragraph.add(table);
		return paragraph;
	};

	private static void insertCell(PdfPTable table, String text, int align, int colspan, Font font,
			boolean isMiddleRow) {
		// create a new cell with the specified Text and Font
		PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
		// set the cell alignment
		cell.setHorizontalAlignment(align);
		// set the cell column span in case you want to merge two or more cells
		cell.setColspan(colspan);
		// in case there is no text and you wan to create an empty row
		if (text.trim().equalsIgnoreCase("")) {
			cell.setMinimumHeight(10f);
		}
		if (isMiddleRow)
			cell.setFixedHeight(55f);
		// add the call to the table
		table.addCell(cell);

	}
}