# sbt-code-quality: SBT plugin #

This is a fork of the sbt-code-quality.g8 project, built as a SBT plugin.

It adds support for [checkstyle](http://checkstyle.sourceforge.net/) and
[pmd](http://pmd.sourceforge.net/) to SBT.

## Setup ##

add following line to `project/plugins.sbt`

    addSbtPlugin("de.corux" %% "sbt-code-quality" % "0.2.0")
    resolvers += "corux-releases" at "https://nexus.corux.de/content/repositories/releases/"
    resolvers += "corux-snapshots" at "https://nexus.corux.de/content/repositories/snapshots/"

and inject plugin settings into project in `build.sbt`:

    codequality.CodeQualityPlugin.Settings

## Usage ##

### Checkstyle ###

You can check your code with [checkstyle](http://checkstyle.sourceforge.net/)
by typing `sbt checkstyle`. The result file is `target/checkstyle-result.xml`.

You must have a checkstyle configuration file under your projects root
folder. The file must be called `checkstyle-config.xml`.

### PMD ###

You can check your code with [pmd](http://pmd.sourceforge.net/) by typing
`sbt pmd`. The result file is `target/pmd.xml`.

You must have a pmd rule set under your projects root folder. The file
must be called `pmd-ruleset.xml`.
