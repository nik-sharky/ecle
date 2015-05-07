package org.ploys.ecle.prefs;

import java.util.Collection;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Class is intended to create a Group Widgets, inside of the
 * {@link FieldEditorPreferencePage} objects. This class should be used as
 * following:
 * 
 * use the {@link #getFieldEditorParent()} to as a parent, while creating new
 * Field Editors.
 * 
 * use {@link #setFieldEditors(Collection)} to add the collection of
 * FieldEditors to the {@link GroupFieldEditor}.
 * 
 *
 * @author alf
 *
 */
public class GroupFieldEditor extends FieldEditor {
	private String name;
	private int numcolumns;
	private Group group;
	private Composite parent;
	private Collection<FieldEditor> members;

	/**
	 * The gap outside, between the group-frame and the widgets around the group
	 */
	private static final int GROUP_PADDING = 5; // px

	/**
	 * The gap inside, between the group-frame and the content
	 */
	private static final int GROUP_VERTICAL_MARGIN = 5; // px

	/**
	 * The inside-distance creates a new boolean field editor
	 */
	protected GroupFieldEditor() {
	}

	/**
	 * Creates a Group of {@link FieldEditor} objects
	 *
	 * @param name
	 *            - name
	 * @param fieldEditorParent
	 *            - parent
	 */
	public GroupFieldEditor(String name, Composite fieldEditorParent) {
		this.name = name;

		// the parent is a Composite, which is contained inside of the
		// preference page. Initially it
		// does not have any layout.
		this.parent = fieldEditorParent;
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = GROUP_VERTICAL_MARGIN;
		this.parent.setLayout(fillLayout);

		this.group = new Group(parent, SWT.DEFAULT);
		this.group.setText(this.name);
	}

	/**
	 * The parent for all the FieldEditors inside of this Group.
	 *
	 * @return - the parent
	 */
	public Composite getFieldEditorParent() {
		return group;
	}

	/**
	 * Sets the FieldeditorChildren for this {@link GroupFieldEditor}
	 * 
	 * @param membersParam
	 */
	public void setFieldEditors(Collection<FieldEditor> membersParam) {
		this.members = membersParam;
		doFillIntoGrid(getFieldEditorParent(), numcolumns);
	}

	public void addFieldEditor(FieldEditor field) {
		this.members.add(field);
		doFillIntoGrid(getFieldEditorParent(), numcolumns);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
		this.numcolumns = numColumns;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doFillIntoGrid(Composite parentParam, int numColumns) {

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginLeft = GROUP_PADDING;
		gridLayout.marginRight = GROUP_PADDING;
		gridLayout.marginTop = GROUP_PADDING;
		gridLayout.marginBottom = GROUP_PADDING;
		this.group.setLayout(gridLayout);

		this.parent.layout();
		this.parent.redraw();

		if (members != null) {
			for (FieldEditor editor : members) {
				editor.fillIntoGrid(getFieldEditorParent(), 1);
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor. Loads the value from the
	 * preference store and sets it to the check box.
	 */
	@Override
	protected void doLoad() {
		if (members != null) {
			for (FieldEditor editor : members) {
				editor.load();
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor. Loads the default value
	 * from the preference store and sets it to the check box.
	 */
	@Override
	protected void doLoadDefault() {
		if (members != null) {
			for (FieldEditor editor : members) {
				editor.loadDefault();
			}
		}
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	protected void doStore() {
		if (members != null) {
			for (FieldEditor editor : members) {
				editor.store();
			}
		}
	}

	@Override
	public void store() {
		super.store();
		doStore();
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	public int getNumberOfControls() {
		return 1;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	@Override
	public void setFocus() {
		if (members != null && !members.isEmpty()) {
			members.iterator().next().setFocus();
		}
	}

	/*
	 * @see FieldEditor.setEnabled
	 */
	@Override
	public void setEnabled(boolean enabled, Composite parentParam) {
		if (members != null) {
			for (FieldEditor editor : members) {
				editor.setEnabled(enabled, parentParam);
			}
		}
	}

	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		super.setPreferenceStore(store);
		if (members != null) {
			for (FieldEditor editor : members) {
				editor.setPreferenceStore(store);
			}
		}
	}

}
