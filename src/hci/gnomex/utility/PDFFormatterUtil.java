package hci.gnomex.utility;

import hci.gnomex.controller.GNomExFrontController;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PDFFormatterUtil {
	
  protected static final float     TABLE_PERCENT_WIDTH = 100;
  
	public static Image makeRequestCategoryImage(DictionaryHelper dictionaryHelper, Request request, float width, float height) {
		RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(request.getCodeRequestCategory());
		String imageName = requestCategory.getIcon();
		String appUrl = GNomExFrontController.getWebContextPath();
		
		if (imageName == null || imageName.equals("")) {
			imageName = "flask.png";
		} else if (imageName.startsWith("assets/")) {
			// Remove leading assets directory
			// We serve images for the report from the webapp root /images directory
			imageName = imageName.substring(7);
		}
		
		Image image = null;
		try {
			BufferedImage img;
			try {
				img = ImageIO.read(new File(appUrl + "images/" + imageName));
			} catch (IOException e) {
				img = ImageIO.read(new File(appUrl + "images/flask.png"));
			}
			image = Image.getInstance(img, null);
			image.scaleAbsolute(width, height);
			image.setAlignment(Element.ALIGN_CENTER);
		} catch (Exception e) {

		    image = null;
		} 
		
		return image;
	}
	
	public static void formatTable(PdfPTable table, float percentWidth) {
	  table.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.setWidthPercentage( percentWidth );
    table.setSpacingBefore( 5 );
  }
	
	public static void addToTable(PdfPTable table, String text, Font font, int hAlignment, int vAlignment, boolean borderTop, boolean borderRight, boolean borderBottom, boolean borderLeft, BaseColor borderColor, int colSpan, int rowSpan) {
		addToTable(table, new PdfPCell(new Phrase(text, font)), hAlignment, vAlignment, borderTop, borderRight, borderBottom, borderLeft, borderColor, colSpan, rowSpan);
	}
	
	public static void addToTable(PdfPTable table, PdfPCell c, int hAlignment, int vAlignment, boolean borderTop, boolean borderRight, boolean borderBottom, boolean borderLeft, BaseColor borderColor, int colSpan, int rowSpan) {
		PdfPCell cell = new PdfPCell(c);
		
		cell.setHorizontalAlignment(hAlignment);
		cell.setVerticalAlignment(vAlignment);
		
		cell.setBorder(Rectangle.NO_BORDER);
		if (borderTop) cell.enableBorderSide(Rectangle.TOP);
		if (borderRight) cell.enableBorderSide(Rectangle.RIGHT);
		if (borderBottom) cell.enableBorderSide(Rectangle.BOTTOM);
		if (borderLeft) cell.enableBorderSide(Rectangle.LEFT);
		cell.setBorderColor(borderColor);
		
		// In case there is no text
		if (c.getPhrase() != null && c.getPhrase().getContent() != null && c.getPhrase().getContent().trim().equals("")) {
			cell.setMinimumHeight(10f);
		}
		
		cell.setColspan(colSpan);
		cell.setRowspan(rowSpan);
		
		table.addCell(cell);
	}	
	
	public static void addToTableHeader(PdfPTable table, String text, Font font) {
		addToTable(table, text, font, Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, false, false, true, false, BaseColor.BLACK, 1, 1);
	}
	
	public static void addToTableValue(PdfPTable table, String text, Font font) {
		addToTable(table, text, font, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, true, true, true, true, BaseColor.BLACK, 1, 1);
	}
	
	public static void addToTablePaddingCell(PdfPTable table, int colSpan, int rowSpan) {
		addToTable(table, "", new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK), Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, false, false, false, false, BaseColor.BLACK, colSpan, rowSpan);
	}
	
	public static void addToTableBlankRow(PdfPTable table) {
		addToTablePaddingCell(table, table.getNumberOfColumns(), 1);
	}
	
	public static void padNestedTables(PdfPTable table1, PdfPTable table2) {
		int table1Rows = table1.getRows().size();
		int table2Rows = table2.getRows().size();
		
		// Special case where each table has 1 row
		if (table1Rows == 1 && table2Rows == 1) {
			float table1Height = table1.getRows().get(0).getCells()[0].getHeight();
			float table2Height = table2.getRows().get(0).getCells()[0].getHeight();
			if (table1Height > table2Height) {
				table2.getRows().get(0).getCells()[0].setMinimumHeight(table1Height);
			} else if (table2Height > table1Height) {
				table1.getRows().get(0).getCells()[0].setMinimumHeight(table2Height);
			}
		} 
		
		else {
			if (table1Rows > table2Rows) {
				addToTablePaddingCell(table2, table2.getNumberOfColumns(), table1Rows - table2Rows);
			} else if (table2Rows > table1Rows) {
				addToTablePaddingCell(table1, table1.getNumberOfColumns(), table2Rows - table1Rows);
			}
		}
	}
	
	public static PdfPTable combineTables(PdfPTable table1, PdfPTable table2) {
		return combineTables(table1, table2, Element.ALIGN_CENTER, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, Element.ALIGN_MIDDLE, false, false, BaseColor.BLACK, BaseColor.BLACK);
	}
	
	public static PdfPTable combineTables(PdfPTable table1, PdfPTable table2, int leftTableVAlign, int rightTableVAlign) {
		return combineTables(table1, table2, Element.ALIGN_CENTER, Element.ALIGN_CENTER, leftTableVAlign, rightTableVAlign, false, false, BaseColor.BLACK, BaseColor.BLACK);
	}
	
	public static PdfPTable combineTables(PdfPTable table1, PdfPTable table2, int leftTableHAlign, int rightTableHAlign, int leftTableVAlign, int rightTableVAlign, boolean leftTableBorder, boolean rightTableBorder, BaseColor leftTableBorderColor, BaseColor rightTableBorderColor) {
		PdfPTable combinedTable = new PdfPTable(2);
		padNestedTables(table1, table2);
		addToTable(combinedTable, new PdfPCell(table1), leftTableHAlign, leftTableVAlign, leftTableBorder, leftTableBorder, leftTableBorder, leftTableBorder, leftTableBorderColor, 1, 1);
		addToTable(combinedTable, new PdfPCell(table2), rightTableHAlign, rightTableVAlign, rightTableBorder, rightTableBorder, rightTableBorder, rightTableBorder, rightTableBorderColor, 1, 1);
		return combinedTable;		
	}
	
	public static Element makeTableTitle(String title, Font font) {
		Paragraph tableTitle = new Paragraph();
		tableTitle.setAlignment(Element.ALIGN_LEFT);
		tableTitle.setFont(font);
		tableTitle.add(title);
		return tableTitle;
	}
	
	public static void addPhrase(List<Element> list, String text, Font font) {
		if (!list.isEmpty()) list.add(Chunk.NEWLINE);
		list.add(new Phrase(text, font));
	}
	
	public static void addCCNumberCell(String ccNumber, PdfPTable table, Font font, DictionaryHelper dictionaryHelper) {
		Anchor ccNumberAnchor = new Anchor(ccNumber, font);
		ccNumberAnchor.setReference(dictionaryHelper.getPropertyDictionary(PropertyDictionary.GNOMEX_LINKAGE_BST_URL) + "#ccNumber=" + ccNumber);
		
		PdfPCell cell = new PdfPCell(ccNumberAnchor);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		table.addCell(cell);
	}
	
	public static String stripRichText(String richText) {
		String plainText;
		if (richText == null || richText.trim().equals("")) {
			plainText = "";
		}
		else {
			try {
				HTMLEditorKit parser = new HTMLEditorKit();
				StringReader reader = new StringReader(richText);
				HTMLDocument document = new HTMLDocument();
				parser.read(reader, document, 0);
				plainText = document.getText(0, document.getLength());
			} catch (Exception e) {
				plainText = "";

			}
		}
		return plainText;
	}
	
	public static String stripEndOfLineCharacters(String text) {
		String newText = text;
		
		newText = newText.replaceAll("\\r", "");
		newText = newText.replaceAll("\\n", "");
		
		return newText;
	}

}
