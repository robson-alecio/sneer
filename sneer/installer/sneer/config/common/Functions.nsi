;-----------------------------------
;JRE FUNCTIONS

!define JRE_VERSION "1.6"
!define JRE_URL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=11292"

Function GetJRE
        MessageBox MB_OK "$(STRING_JAVAWILLBEDOWNLOADED)"
        StrCpy $2 "$TEMP\Java Runtime Environment.exe /s ADDLOCAL=ALL IEXPLORER=1 MOZILLA=1"
        nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
        Pop $R0 ;Get the return value
                StrCmp $R0 "success" +3
                MessageBox MB_OK "Download failed: $R0"
                Quit
        ExecWait $2
        Delete $2
FunctionEnd
 
Function DetectJREAndDownloadIfNeeded
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $2 ${JRE_VERSION} done
  Call GetJRE
  done:
FunctionEnd

Function DetectAndLaunchJREInstallerIfNeeded
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $2 ${JRE_VERSION} done
  Call LaunchJREInstaller
  done:
FunctionEnd

Function OnlyWorksWithJREInstalled
	ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
	StrCmp $2 ${JRE_VERSION} done
		MessageBox MB_OK "$(STRING_JAVANOTINSTALLED)"
    	Quit
  	done:
FunctionEnd

;-----------------------------------
;INSTALLER FUNCTIONS

Function InstallInAddRemovePrograms
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${InstallName}" "DisplayName" "${InstallName} - ${Description}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${InstallName}" "UninstallString" "${Uninst}"
FunctionEnd

Function un.RemoveFromAddRemovePrograms
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${InstallName}"
FunctionEnd


Function CreateStartMenuShortcut
	CreateDirectory "$SMPROGRAMS\${ShortcutFolder}"
	CreateShortCut "$SMPROGRAMS\${ShortcutFolder}\${ShortcutFile}" "$INSTDIR\${ShortcutTarget}" "" "$INSTDIR\${ShortcutIcon}"
FunctionEnd

Function CreateDesktopShortcut
	CreateShortCut "$DESKTOP\${ShortcutFile}" "$INSTDIR\${ShortcutTarget}" "" "$INSTDIR\${ShortcutIcon}"
FunctionEnd

Function un.RemoveStartMenuShortcut
	Delete "$SMPROGRAMS\${ShortcutFolder}\${ShortcutFile}"
	RMDir "$SMPROGRAMS\${ShortcutFolder}"
FunctionEnd

Function un.RemoveDesktopShortcut
	Delete "$DESKTOP\${ShortcutFile}"
FunctionEnd

Function CreateUninstaller
	writeuninstaller "${Uninst}"
FunctionEnd

Function un.RemoveUninstaller
	Delete "${Uninst}"
FunctionEnd

Function un.RemoveInstallDir
	RmDir "$INSTDIR"
FunctionEnd

Function LaunchLink
	MessageBox MB_OK "$(STRING_SNEERCONFIGURATION)"
	ExecShell "" "$SMPROGRAMS\${ShortcutFolder}\${ShortcutFile}"
FunctionEnd

Function LaunchJREInstaller
	DetailPrint "$(STRING_JREINSTALLWILLTAKESOMETIME)"
	SetDetailsPrint none
	ExecWait '"$INSTDIR\${JREFileName}" /s ADDLOCAL=ALL IEXPLORER=1 MOZILLA=1'
	SetDetailsPrint both
FunctionEnd
