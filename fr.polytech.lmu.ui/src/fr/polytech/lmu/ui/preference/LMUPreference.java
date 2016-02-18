package fr.polytech.lmu.ui.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fr.polytech.lmu.ui.Activator;

public class LMUPreference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
		
	public static String[][] extensions = new String[][] { 
		{"PDF", "pdf"}, 
		{"PNG", "png"}, 
		{"SVG", "svg"},
		{"DOT", "dot"},
		{"Java", "java"},
		{"PS", "ps"},
		{"FIG", "fig"},
		{"LMU", "lmu"}
	};
	
	public LMUPreference(){
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		addField(new RadioGroupFieldEditor("outputExtension", "Choose desired output extension", 1, extensions, getFieldEditorParent()));
		addField(new DirectoryFieldEditor("outputDirectory", "Choose output directory ", getFieldEditorParent()));
		addField(new StringFieldEditor("outputName", "Enter file output's name ", getFieldEditorParent()));
		addField(new IntegerFieldEditor("wishedDepth", "Select wished depth for deployment units anaysis ", getFieldEditorParent()));
	}
	
	public class LMUPreferenceInitializer extends AbstractPreferenceInitializer {
		
		public LMUPreferenceInitializer(){
			super();
		}
		
		/** 
		 * Initializes a preference store with default preference values 
		 * for this plug-in.
		 */
		@Override 
		public void initializeDefaultPreferences() {
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			store.setDefault("outputExtension", "pdf");			
			store.setDefault("outputDirectory", "lmu/");			
			store.setDefault("outputName", "output");	
			store.setDefault("wishedDepth", "1");
		}
		
	}
	
}
