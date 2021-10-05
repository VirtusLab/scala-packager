package packager.rpm

case class RedHatSpecPackage(
    launcherAppName: String,
    version: String,
    description: String,
    buildArch: String,
    license: String,
    release: String
) {

  def generateContent: String =
    s"""Name:           $launcherAppName
       |Version:        $version
       |Release:        $release
       |Summary:        $description
       |BuildArch:      $buildArch
       |
       |License:        $license
       |
       |#BuildRequires:
       |Requires:       bash
       |
       |%define _rpmfilename %%{NAME}.rpm
       |
       |%description
       |RedHat package
       |
       |%define _binaries_in_noarch_packages_terminate_build 0
       |
       |%install
       |rm -rf $$RPM_BUILD_ROOT
       |mkdir -p $$RPM_BUILD_ROOT/%{_bindir}
       |cp ./rpmbuild/SOURCES/$launcherAppName $$RPM_BUILD_ROOT/%{_bindir}
       |
       |%clean
       |rm -rf $$RPM_BUILD_ROOT
       |
       |%files
       |%{_bindir}/$launcherAppName
       |""".stripMargin

}
