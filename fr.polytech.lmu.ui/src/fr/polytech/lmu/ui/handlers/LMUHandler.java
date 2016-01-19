package fr.polytech.lmu.ui.handlers;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.lucci.lmu.*;
import org.lucci.lmu.input.JavaFileListAnalyser;
import org.lucci.lmu.input.ModelAnalyser;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import fr.polytech.lmu.ui.Activator;
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
	
	protected static String OUTPUT_DIRECTORY; 
	protected static String OUTPUT_EXTENSION;
	protected static String OUTPUT_NAME;
	
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

		OUTPUT_EXTENSION = Activator.getDefault().getPreferenceStore().getString("outputExtension");
		OUTPUT_DIRECTORY = Activator.getDefault().getPreferenceStore().getString("outputDirectory");
		OUTPUT_NAME = Activator.getDefault().getPreferenceStore().getString("outputName");
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		MessageDialog.openInformation(
				window.getShell(),
				"lmu-ui",
				"Your file (" + OUTPUT_NAME + "." + OUTPUT_EXTENSION + ") will be registered in the following directory : " + OUTPUT_DIRECTORY);
		
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
					
					inputType = "javalist";
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
						new RegularFile(OUTPUT_DIRECTORY + "/" + OUTPUT_NAME + "." + OUTPUT_EXTENSION);
				AbstractWriter factory = AbstractWriter.getTextFactory(OUTPUT_EXTENSION);
				
				if (factory == null) {
					System.out.println("Do not know how to generate '" + OUTPUT_EXTENSION + "' code\n");
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
