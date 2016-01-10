package fr.polytech.lmu.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class LMUHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public LMUHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		String outputPath = event.getParameter("fr.polytech.lmu.ui.outputPath");
		
		MessageDialog.openInformation(
				window.getShell(),
				"lmu-ui",
				"Your file will be registered at your project root in \"lmu\" directory");
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return super.isEnabled();
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return super.isHandled();
	}
	
	
}
