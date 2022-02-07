# Scala Packager

Submodule of ScalaCLI to package application in native formats.

The main goal of this project is to create simple scala library which packaging binary app to the following formats:
* Linux
  * RedHat 
  * Debian
* MacOS
  * Pkg
  * Dmg
* Windows
  * MSI
* Docker

## Modules

The project consists of two dependent modules

### Cli

Provides the command line application interface to building native formats. It is used in [scala-cli](https://github.com/VirtusLab/scala-cli/blob/main/.github/scripts/generate-os-packages.sh) for generating os package.

### Packager

Core library for generating specific native package.  
