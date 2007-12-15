!include "MUI.nsh"

;--------------------------------
;General

Name "${InstallName}"
InstallDir "$PROGRAMFILES\Sneer"
outfile "../build/${InstallerFile}"
!define Uninst "$INSTDIR\${UninstallerFile}"

!include "common\Language.nsi"
!include "common\Functions.nsi"

;--------------------------------
;Interface Configuration
  !define MUI_ICON "${NSISDIR}\..\sneer\resources\${ShortcutIcon}"
  !define MUI_HEADERIMAGE
  !define MUI_HEADERIMAGE_BITMAP "${NSISDIR}\..\sneer\resources\splash.bmp" ; optional
  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
  !insertmacro LANGUAGE_CONFIG
  !insertmacro MUI_RESERVEFILE_LANGDLL

;--------------------------------
;Installer Functions

Function .onInit
  	!insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

;--------------------------------
