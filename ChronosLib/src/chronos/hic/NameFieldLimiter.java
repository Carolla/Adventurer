package chronos.hic;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Replaces the default DocumentFilter for the input value of the Hero's name
 * field. It limits the allowed input to the length specified by HERO_NAME_WIDTH
 * and causes an audio "beep" to be sounded by the system when more input is
 * attempted after the limit is reached.
 * 
 * @author OG
 *
 */
public class NameFieldLimiter extends DocumentFilter {
	
	private final int _length;

	public NameFieldLimiter(int length)
	{
		_length = length;
	}
	
	@Override
	public void insertString(DocumentFilter.FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException {
		if (fb.getDocument().getLength() + string.length() > _length) {
			System.out.println("Insert String: Name Field Limit Reached");
			java.awt.Toolkit.getDefaultToolkit().beep();
		} else {
			fb.insertString(offset, string, attr);
		}
	}

	@Override
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String text, AttributeSet attrs) throws BadLocationException {
		if (fb.getDocument().getLength() + text.length() > _length) {
			System.out.println("Replace: Name Field Limit Reached");
			java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		} else {
			fb.insertString(offset, text, attrs);
		}
	}
} // end NameFieldLimiter

