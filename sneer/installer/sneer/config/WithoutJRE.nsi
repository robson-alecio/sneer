;-------------------------------------

!define InstallName "Sneer"
!define Description "Sovereign Computing"
!define ShortcutFolder "Sneer"
!define ShortcutFile "Sneer.lnk"
!define ShortcutTarget "Sneer-build-candidate.jar"
!define ShortcutIcon "sneer.ico"
!define InstallerFile "WithoutJRE.exe"
!define UninstallerFile "uninstall.exe"
!define JREFileName "jre-6u3-windows-i586-p-s.exe"

;-------------------------------------

RequestExecutionLevel admin
!include "common\UI.nsi"

;-------------------------------------

Section Install

	Call OnlyWorksWithJREInstalled

	;install files
	SetOutPath "$INSTDIR"
	
	file "${NSISDIR}\..\sneer\resources\${ShortcutTarget}"
	file "${NSISDIR}\..\sneer\resources\${ShortcutIcon}"
		
	Call CreateUninstaller
	Call CreateStartMenuShortcut
	Call CreateDesktopShortcut
	Call InstallInAddRemovePrograms
	Call MakeRunOnWindowsStart
	Call LaunchLink
	
SectionEnd

;-------------------------------------

Section Uninstall
	Call un.RemoveUninstaller
	Call un.RemoveStartMenuShortcut
	Call un.RemoveDesktopShortcut
	Call un.RemoveInstallDir
	Call un.RemoveFromAddRemovePrograms
	Call un.RemoveRunOnWindowsStart
SectionEnd


