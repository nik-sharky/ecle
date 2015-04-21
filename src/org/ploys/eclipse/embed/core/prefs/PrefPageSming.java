package org.ploys.eclipse.embed.core.prefs;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.ploys.eclipse.embed.ui.Activator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class PrefPageSming extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PrefPageSming() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("General settings");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();

		//Group grpPath = createGroup(getFieldEditorParent(), "Sming path definition");
		addField(new DirectoryFieldEditor(PrefKeys.Sming.PATH, "&Sming framework path:", parent));
//		addField(new DirectoryFieldEditor(PrefKeys.Path.COMPILER, "&Compiler path:", grpPath));
//		addField(new DirectoryFieldEditor(PrefKeys.Path.TOOLS, "&Tools path:", grpPath));	
		
		/*
		Group grpSerial = createGroup(parent, "General serial port settings");
		addField(new StringFieldEditor(PrefKeys.Serial.PORT, "Default port:", grpSerial));
		addField(new RadioGroupFieldEditor(PrefKeys.Serial.PARITY, "Parity", 6, PrefKeys.Serial.VPARITY, grpSerial));
		addField(new RadioGroupFieldEditor(PrefKeys.Serial.DATABITS, "Data bits", 6, PrefKeys.Serial.VDATABITS,
				grpSerial));
		addField(new RadioGroupFieldEditor(PrefKeys.Serial.STOPBITS, "Stop bits", 2, PrefKeys.Serial.VSTOPBITS,
				grpSerial));

		// addField(new BooleanFieldEditor(PrefKeys.P_BOOLEAN,
		// "&An example of a boolean preference", grpSerial));
		// addField(new StringFieldEditor(PrefKeys.P_STRING,
		// "A &text preference:", getFieldEditorParent()));
		this.adjustGridLayout();
		*/
	}

	private Group createGroup(Composite parent, String title) {
		Group grp = new Group(parent, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		grp.setLayoutData(gridData);

		// GridLayout layout = new GridLayout(2, false);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 5;
		layout.marginWidth = 15;
		layout.spacing = 50;
		
		grp.setLayout(layout);
		grp.setText(title);

		return grp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}