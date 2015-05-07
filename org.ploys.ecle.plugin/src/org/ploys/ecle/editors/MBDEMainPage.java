package org.ploys.ecle.editors;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.cdtvariables.ICdtVariableManager;
import org.eclipse.cdt.core.envvar.IEnvironmentVariableManager;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.ResourceManager;
import org.ploys.ecle.Activator;

public class MBDEMainPage extends FormPage {

	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public MBDEMainPage(String id, String title) {
		super(id, title);
	}

	/**
	 * Create the form page.
	 * @param editor
	 * @param id
	 * @param title
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter id "Some id"
	 * @wbp.eval.method.parameter title "Some title"
	 */
	public MBDEMainPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		managedForm.getForm().setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/full/eview16/chip/cpu.png"));
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Settings");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(2, false));
		
		Composite composite = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));
		
		Section section = managedForm.getToolkit().createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(section);
		section.setText("Build settings");
		section.setExpanded(true);
		
		Section section_1 = managedForm.getToolkit().createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
		section_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		section_1.setBounds(0, 0, 98, 21);
		managedForm.getToolkit().paintBordersFor(section_1);
		section_1.setText("Serial settings");
		section_1.setExpanded(true);
		
		Composite composite_1 = managedForm.getToolkit().createComposite(managedForm.getForm().getBody(), SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(composite_1);
		
		Section section_2 = managedForm.getToolkit().createSection(composite_1, Section.TWISTIE | Section.TITLE_BAR);
		section_2.setBounds(0, 0, 98, 21);
		managedForm.getToolkit().paintBordersFor(section_2);
		section_2.setText("Related info");
		section_2.setExpanded(true);
		
		IToolBarManager manager = form.getToolBarManager();

		Action myAction = buildAction(); 
		myAction.setId("act1");
		myAction.setToolTipText("build");
		myAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor("org.eclipse.rse.ui", "/icons/full/elcl16/run.gif"));

		manager.add(myAction);
		manager.update(true);
	}
	
	private Action buildAction() {
		return new Action("My Action") {
		    public void run() {
		        
		    	try {
					buildProject();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		};
	}
	
	private void buildProject() throws Exception {
		
		CCorePlugin cp = CCorePlugin.getDefault();
		
		ICdtVariableManager varManager = cp.getCdtVariableManager();
		IEnvironmentVariableManager bem = cp.getBuildEnvironmentManager();
		
		String name = "Ultrasonic_HCSR04";
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		
		IConfiguration activeConfig = ManagedBuildManager.getBuildInfo(project).getDefaultConfiguration();
		activeConfig.setManagedBuildOn(true);  // or false
		ManagedBuildManager.saveBuildInfo(project, true);
		
		//ManagedBuildManager.buildConfigurations(configs, monitor);
		project.build(0, new IProgressMonitor(){

			@Override
			public void beginTask(String name, int totalWork) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void done() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void internalWorked(double work) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean isCanceled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setCanceled(boolean value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setTaskName(String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void subTask(String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void worked(int work) {
				// TODO Auto-generated method stub
				
			}});
		//String resolvedValue = varManager.resolveValue(value, "", null, cfgDescription);
		 
		
	}
}
