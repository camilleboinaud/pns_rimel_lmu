package fr.polytech.lmu.ui.handlers;

import java.io.IOException;
import java.net.MalformedURLException;
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
					System.out.println("Complationunit");
					
					inputType = "javalist";
					
					ICompilationUnit compilationUnit = ((ICompilationUnit) selected);

					readClassNames(compilationUnit, classNames);
					
					ClassLoader classLoader = getClassLoader(compilationUnit.getJavaProject());
					
					analyser = new JavaFileListAnalyser(classLoader, classNames);
					
				} else if (selected instanceof IPackageFragmentRoot) {
					IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) selected;
					
					readClassNames(packageFragmentRoot, classNames);
					
					ClassLoader classLoader = getClassLoader(packageFragmentRoot.getJavaProject());
					
					analyser = new JavaFileListAnalyser(classLoader, classNames);
				} else if (selected instanceof IPackageFragment) {
					System.out.println("packagefragment");
					
					IPackageFragment packageFragment = (IPackageFragment) selected;
					
					readClassNames(packageFragment, classNames);
					
					ClassLoader classLoader = getClassLoader(packageFragment.getJavaProject());
					
					analyser = new JavaFileListAnalyser(classLoader, classNames);
				
					//TODO
				} else if (selected instanceof IJavaProject) {
					
					IJavaProject project = (IJavaProject) selected;
					
					readClassNames(project, classNames);
					
					ClassLoader classLoader = getClassLoader(project);
					
					analyser = new JavaFileListAnalyser(classLoader, classNames);
					
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
	
	// Helpers
	
	private void readClassNames(ICompilationUnit unit, List <String> classNames) throws JavaModelException {
		classNames.add(getFullClassName(unit));
	}
	
	private void readClassNames(IPackageFragment packageFragment, List <String> classNames) throws JavaModelException {
		for (ICompilationUnit unit : packageFragment.getCompilationUnits()) {
			readClassNames(unit, classNames);
		}
	}
	
	private void readClassNames(IPackageFragmentRoot root, List <String> classNames) {
		//TODO
	}
	
	private void readClassNames(IJavaProject project, List <String> classNames) throws JavaModelException {
		for (IPackageFragment packageFragment : project.getPackageFragments()) {
			readClassNames(packageFragment, classNames);
		}
	}
	
	private String getFullClassName(ICompilationUnit unit) throws JavaModelException {
		IPackageDeclaration[] decl = unit.getPackageDeclarations();
		
		String packageDeclared = "";
		for(IPackageDeclaration d: decl){ 
			packageDeclared += d.getElementName() + "."; 					
		}
		
		return packageDeclared + unit.getElementName().split("\\.")[0];
	}
	
	private ClassLoader getClassLoader(IJavaProject project) throws JavaModelException, MalformedURLException {
		IPath outputLocation = project.getOutputLocation();
		String outputFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(outputLocation.makeRelative()).getLocation().toString();
		
		List <URL> urlEntries = new ArrayList<>();
		
		System.out.println("raw : ");
		for (IClasspathEntry entry : project.getRawClasspath()) {
			urlEntries.add(new URL ("file:" + entry.getPath().toString()));
			//System.out.println(entry.getPath());
		}
		
		urlEntries.add(new URL("file:" + outputFolder + "/"));
		
		URL[] urls = new URL[urlEntries.size()];
		urlEntries.toArray(urls);
		
		return new URLClassLoader(urls);
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
