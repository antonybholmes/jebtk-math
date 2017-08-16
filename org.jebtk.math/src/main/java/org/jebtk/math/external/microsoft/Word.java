/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jebtk.math.external.microsoft;

import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

// TODO: Auto-generated Javadoc
/**
 * The class Word.
 */
public class Word {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		XWPFDocument doc = new XWPFDocument();

		XWPFParagraph p1 = doc.createParagraph();
		p1.setVerticalAlignment(TextAlignment.TOP);
		
		XWPFRun r1 = p1.createRun();
		//r1.setBold(true);
		r1.setFontFamily("Courier New");
		r1.setText("The quick brown fox");
		r1.setColor("ff0000");

		XWPFParagraph p2 = doc.createParagraph();
		p2.setAlignment(ParagraphAlignment.RIGHT);

		//BORDERS
		p2.setBorderBottom(Borders.DOUBLE);
		p2.setBorderTop(Borders.DOUBLE);
		p2.setBorderRight(Borders.DOUBLE);
		p2.setBorderLeft(Borders.DOUBLE);
		p2.setBorderBetween(Borders.SINGLE);

		XWPFRun r2 = p2.createRun();
		r2.setText("jumped over the lazy dog");
		r2.setStrikeThrough(true);
		r2.setFontSize(20);

		XWPFRun r3 = p2.createRun();
		r3.setText("and went away");
		r3.setStrikeThrough(true);
		r3.setFontSize(20);
		r3.setSubscript(VerticalAlign.SUPERSCRIPT);


		XWPFParagraph p3 = doc.createParagraph();
		p3.setWordWrap(true);
		p3.setPageBreak(true);

		//p3.setAlignment(ParagraphAlignment.DISTRIBUTE);
		p3.setAlignment(ParagraphAlignment.BOTH);
		p3.setSpacingLineRule(LineSpacingRule.EXACT);

		p3.setIndentationFirstLine(600);


		XWPFRun r4 = p3.createRun();
		r4.setTextPosition(20);
		r4.setText("To be, or not to be: that is the question: "
				+ "Whether 'tis nobler in the mind to suffer "
				+ "The slings and arrows of outrageous fortune, "
				+ "Or to take arms against a sea of troubles, "
				+ "And by opposing end them? To die: to sleep; ");
		r4.addBreak(BreakType.PAGE);
		r4.setText("No more; and by a sleep to say we end "
				+ "The heart-ache and the thousand natural shocks "
				+ "That flesh is heir to, 'tis a consummation "
				+ "Devoutly to be wish'd. To die, to sleep; "
				+ "To sleep: perchance to dream: ay, there's the rub; "
				+ ".......");
		r4.setItalic(true);
		//This would imply that this break shall be treated as a simple line break, and break the line after that word:

		XWPFRun r5 = p3.createRun();
		r5.setTextPosition(-10);
		r5.setText("For in that sleep of death what dreams may come");
		r5.addCarriageReturn();
		r5.setText("When we have shuffled off this mortal coil,"
				+ "Must give us pause: there's the respect"
				+ "That makes calamity of so long life;");
		r5.addBreak();
		r5.setText("For who would bear the whips and scorns of time,"
				+ "The oppressor's wrong, the proud man's contumely,");

		r5.addBreak(BreakClear.ALL);
		r5.setText("The pangs of despised love, the law's delay,"
				+ "The insolence of office and the spurns" + ".......");

		FileOutputStream out = new FileOutputStream("simple.docx");
		doc.write(out);
		out.close();

	}
}
