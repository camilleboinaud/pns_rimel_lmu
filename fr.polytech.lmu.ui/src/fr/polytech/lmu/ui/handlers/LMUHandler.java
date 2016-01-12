package fr.polytech.lmu.ui.handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.lucci.lmu.Model;
import org.lucci.lmu.input.JavaFileListAnalyser;
import org.lucci.lmu.input.ModelAnalyser;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import toools.io.FileUtilities;
import toools.io.file.RegularFile;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class LMUHandler extends AbstractHandler {
	
	protected static final String OUTPUT_FILE_PATH = "lmu/"; 
	protected static String OUTPUT_FILE_EXTENSION = "";
	
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

		OUTPUT_FILE_EXTENSION = event.getParameter("fr.polytech.lmu.ui.outputType");
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		MessageDialog.openInformation(
				window.getShell(),
				"lmu-ui",
				"Your file will be registered in your personnal directory (in \"lmu\" sub-directory)");
		
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getSelection();
		try {
			if (selection instanceof StructuredSelection) {
				Object selected = ((StructuredSelection) selection).getFirstElement();
				
				Object[] paramsInput = null;
				String inputType = null;
				List<String> classNames = new ArrayList<String>();
				
				ModelAnalyser analyser = null;
				
				if(selected instanceof ICompilationUnit) { //Java or equivalent JVM extension types
					
					inputType = FileUtilities.getFileNameExtension(((ICompilationUnit)selected).getElementName());
					IPackageDeclaration[] decl = ((ICompilationUnit) selected).getPackageDeclarations();
					
					String packageDeclared = "";
					for(IPackageDeclaration d: decl){ 
						packageDeclared += d.getElementName() + "."; 					
					}
					
					classNames.add(packageDeclared + ((ICompilationUnit) selected).getElementName().split("\\.")[0]);
					
					ICompilationUnit compilationUnit = ((ICompilationUnit) selected);
					IJavaProject project = compilationUnit.getJavaProject(); 
					IPath outputLocation = project.getOutputLocation();
					String outputFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(outputLocation.makeRelative()).getLocation().toString();
					
					System.out.println(outputFolder);
					
					// classloaders assumes path without ending slash is a jar file
					ClassLoader classLoader = new URLClassLoader(new URL[]{ new URL("file:" + outputFolder + "/") });
					
					analyser = new JavaFileListAnalyser(classLoader, classNames);
					
				} else if (selected instanceof IPackageFragmentRoot) {
					//TODO
				} else if (selected instanceof IPackageFragment) {
					//TODO
				} else if (selected instanceof IJavaProject) {
					//TODO
				}
				
				if (analyser == null) {
					throw new UnsupportedOperationException(selected.toString());
				}
				
				Model model = analyser.analyse();
				RegularFile outputFile = 
						new RegularFile(OUTPUT_FILE_PATH + "output." + OUTPUT_FILE_EXTENSION);
				AbstractWriter factory = AbstractWriter.getTextFactory(OUTPUT_FILE_EXTENSION);
				
				if (factory == null) {
					System.out.println("Do not know how to generate '" + OUTPUT_FILE_EXTENSION + "' code\n");
				} else {
					System.out.println(model.getEntities().size() + " entities and " 
							+ model.getRelations().size() + " relations\n");

					try {
						System.out.println("Writing file " + outputFile.getPath() + "\n");
						byte[] outputBytes = factory.writeModel(model);
						outputFile.setContent(outputBytes);
					} catch (WriterException ex) {
						System.err.println(ex.getMessage() + "'\n");
					} catch (IOException ex) {
						System.err.println("I/O error while writing file " + outputFile.getPath() + "\n");
					}
				}
								
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return null;
	}
	

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}
	

	@Override
	public boolean isHandled() {
		return super.isHandled();
	}	
	
}
