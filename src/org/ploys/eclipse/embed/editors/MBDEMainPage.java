package org.ploys.eclipse.embed.editors;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.cdtvariables.ICdtVariableManager;
import org.eclipse.cdt.core.envvar.IEnvironmentVariableManager;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;

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
		managedForm.getForm().setImage(ResourceManager.getPluginImage("org.ploys.eclipse.embed.core", "icons/full/eview16/chip/cpu.png"));
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Settings");
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(new GridLayout(2, true));
		
		Section sctnNewSection = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Build settings");
		sctnNewSection.setExpanded(true);
		
		Section sctnHelpItems = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		sctnHelpItems.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(sctnHelpItems);
		sctnHelpItems.setText("Related info");
		sctnHelpItems.setExpanded(true);
		
		Hyperlink hprlnkEspForum = managedForm.getToolkit().createHyperlink(sctnHelpItems, "ESP8266 forum", SWT.NONE);
		managedForm.getToolkit().paintBordersFor(hprlnkEspForum);
		sctnHelpItems.setClient(hprlnkEspForum);
		
		Section sctnSerialSettings = managedForm.getToolkit().createSection(managedForm.getForm().getBody(), Section.TWISTIE | Section.TITLE_BAR);
		sctnSerialSettings.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		managedForm.getToolkit().paintBordersFor(sctnSerialSettings);
		sctnSerialSettings.setText("Serial settings");
		sctnSerialSettings.setExpanded(true);
		
		Composite composite = managedForm.getToolkit().createComposite(sctnSerialSettings, SWT.NONE);
		managedForm.getToolkit().paintBordersFor(composite);
		sctnSerialSettings.setClient(composite);
		composite.setLayout(new GridLayout(4, false));
		
		Label lblPort = managedForm.getToolkit().createLabel(composite, "Port", SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setItems(new String[] {"COM1", "COM2", "COM3"});
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		managedForm.getToolkit().adapt(combo);
		managedForm.getToolkit().paintBordersFor(combo);
		combo.select(0);
		
		Button btnRescan = managedForm.getToolkit().createButton(composite, "rescan", SWT.NONE);
		btnRescan.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//IWorkbenchSupport.createBrowser("myId").openURL(url);
				try {
					PlatformUI.getWorkbench().getBrowserSupport().createBrowser("myId").openURL(new URL("http://esp8266.ru"));
				} catch (PartInitException | MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		Label lblSpeed = managedForm.getToolkit().createLabel(composite, "Speed", SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Combo combo_1 = new Combo(composite, SWT.NONE);
		combo_1.setItems(new String[] {"9600", "115200", "230400"});
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		managedForm.getToolkit().adapt(combo_1);
		managedForm.getToolkit().paintBordersFor(combo_1);
		combo_1.select(1);
		new Label(composite, SWT.NONE);
		new Label(managedForm.getForm().getBody(), SWT.NONE);
		
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
