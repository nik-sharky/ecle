package org.ploys.ecle.builder;

import javax.annotation.PreDestroy;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ProjectManagerView extends ViewPart {
	private static class ContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return new Object[0];
		}
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			return super.getText(element);
		}
	}
	public ProjectManagerView() {
	}

	@PreDestroy
	public void dispose() {

	}

	Combo cProjects;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(3, false));

		Label lblProject = new Label(parent, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProject.setText("Project");

		cProjects = new Combo(parent, SWT.NONE);
		cProjects.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button bSync = new Button(parent, SWT.NONE);
		bSync.setText("Sync");
		new Label(parent, SWT.NONE);
		
		ListViewer listViewer = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		listViewer.setContentProvider(new ContentProvider());
		listViewer.setLabelProvider(new ViewerLabelProvider());
		new Label(parent, SWT.NONE);
	

		bSync.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				updateProjects();
			}
		});

	}

	private void updateProjects() {
		IProject[] projects = getProjects();
		String[] items = new String[projects.length];
		for (int i=0; i<projects.length; i++) {
			items[i] = projects[i].getName();
		}
		
		cProjects.setItems(items);
	}

	public IProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return projects;
	}

	@Override
	public void setFocus() {

	}

}
