# Scala Packager
Submodule of [Scala CLI](https://github.com/Virtuslab/scala-cli) used to package applications in native formats.

The main goal of this project is to create simple scala library which packages binary apps in the following formats:
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
