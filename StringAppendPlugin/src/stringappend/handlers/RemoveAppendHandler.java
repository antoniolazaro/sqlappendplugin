package stringappend.handlers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RemoveAppendHandler extends AbstractHandler {

	/**
	 * The constructor.
	 */
	public RemoveAppendHandler() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {

			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

			if (part instanceof ITextEditor) {

				final ITextEditor editor = (ITextEditor) part;
//				IDocumentProvider prov = editor.getDocumentProvider();
//				IDocument doc = prov.getDocument(editor.getEditorInput());
				
				ISelection sel = editor.getSelectionProvider().getSelection();
				
				if (sel instanceof TextSelection) {
					
					final TextSelection textSel = (TextSelection) sel;
					
					if (textSel.getText() != null) {
						
						char[] text = textSel.getText().toCharArray();
						StringBuilder newText = new StringBuilder();
						
						boolean inclui = false;
						
						for (int i = 0; i < text.length; i++) {
							
							char chr = text[i];
							
							if (chr == '\"') {
								
								inclui = ! inclui;
								continue;

							}
							
							if (inclui || chr == '\t' || chr == '\n' || chr == '\r') {
								newText.append(chr);
							}
							
						}
						
//						doc.replace(textSel.getOffset(), textSel.getLength(), newText.toString());
						
						StringSelection stringSelection = new StringSelection(newText.toString());
					    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					    clipboard.setContents(stringSelection, null);
						
						IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
						MessageDialog.openInformation(
								window.getShell(),
								"Alerta",
								"Texto copiado para a Area de transferencia");
						
					}
					
				}
				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;

	}

}
