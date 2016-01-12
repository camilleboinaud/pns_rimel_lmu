package fr.polytech.lmu.page.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import fr.polytech.lmu.page.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_CHOICE, "SvgChoice");
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				
				if (event.getProperty() == PreferenceConstants.P_CHOICE) {
			        String extensionvalue = event.getNewValue().toString();
			        System.out.println(extensionvalue);
			       /*TODO : Quand Le choix change, on l'a dans extensionvalue (PngChoice : png) (SvgChoice : svg) ect...*/
			    }
				
				if (event.getProperty() == PreferenceConstants.P_PATH) {
			        String pathvalue = event.getNewValue().toString();
			        System.out.println(pathvalue);
			       /*TODO : Quand Le choix change, on l'a dans pathvalue */
			    }
				
				
			}
		});
		

		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
	}

}
