package stringappend.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AddAppendHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public AddAppendHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {

			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

			if (part instanceof ITextEditor) {

				final ITextEditor editor = (ITextEditor) part;
				IDocumentProvider prov = editor.getDocumentProvider();
				IDocument doc = prov.getDocument(editor.getEditorInput());
				
				ISelection sel = editor.getSelectionProvider().getSelection();
				
				if (sel instanceof TextSelection) {
					
					final TextSelection textSel = (TextSelection) sel;
					
					if (textSel.getText() != null && textSel.getText().trim().length() > 0) {
						
						char[] text = textSel.getText().toCharArray();
						StringBuilder newText = new StringBuilder();
						
						boolean tabulacaoInicial = false;
						
						for (int i = 0; i < text.length; i++) {
							
							char chr = text[i];
							
							if (chr != '\t' && tabulacaoInicial == false) {
								
								newText.append("sql.append(\" ");
								// terminou a tabulação inicial
								tabulacaoInicial = true;
								
								newText.append(chr);
								
								continue;
								
							}
							
							if (chr == '\n') {

								newText.append(" \");");
								
								if (i > 0 && text[i-1] == '\r') {
									// se o caractere anterior for \r, imprime ele (tratamento para \r\n)
									newText.append(text[i-1]);
								}
								
								newText.append(chr);
								
								// ignora os \t
								while (i < (text.length - 1) && text[i+1] == '\t') {
									i++;
									newText.append(text[i]);
								}
								
								// insere o append
								newText.append("sql.append(\" ");
								
							} else if (chr == '\r') {
								// ignora pois o tratamento do \r eh feito no \n
							} else {
								newText.append(chr);
							}
							
						}
						
						newText.append(" \");");
						
						doc.replace(textSel.getOffset(), textSel.getLength(), newText.toString());
						
					}
					
				}
				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;

	}

}
