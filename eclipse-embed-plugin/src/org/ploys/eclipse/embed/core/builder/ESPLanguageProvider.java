package org.ploys.eclipse.embed.core.builder;

import java.util.List;

import org.eclipse.cdt.core.language.settings.providers.ILanguageSettingsEditableProvider;
import org.eclipse.cdt.core.language.settings.providers.ILanguageSettingsProvider;
import org.eclipse.cdt.core.language.settings.providers.LanguageSettingsGenericProvider;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.internal.core.envvar.EnvironmentVariableManager;
import org.eclipse.cdt.managedbuilder.language.settings.providers.ToolchainBuiltinSpecsDetector;
import org.eclipse.core.resources.IResource;

public class ESPLanguageProvider extends ToolchainBuiltinSpecsDetector implements ILanguageSettingsEditableProvider {
    // ID must match the tool-chain definition in
    // org.eclipse.cdt.managedbuilder.core.buildDefinitions extension point
    private static final String GCC_TOOLCHAIN_ID = "cdt.managedbuild.toolchain.gnu.base"; //$NON-NLS-1$

	@Override
	public ESPLanguageProvider cloneShallow() throws CloneNotSupportedException {	
		return (ESPLanguageProvider) super.cloneShallow();
	}

	@Override
	public ESPLanguageProvider clone() throws CloneNotSupportedException {
		return (ESPLanguageProvider) super.clone();
	}

	@Override
	public String getToolchainId() {
		return GCC_TOOLCHAIN_ID;
	}

	@Override
	protected List<String> parseOptions(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AbstractOptionParser[] getOptionParsers() {
		// TODO Auto-generated method stub
		return null;
	}
    	

	
}
