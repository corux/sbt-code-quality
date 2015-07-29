package codequality

import sbt._
import Keys._

object CodeQualityPlugin extends Plugin {

  def Settings: Seq[sbt.Project.Setting[_]] = List(
    CheckStyleSettings.all,
    PmdSettings.all
  ).flatten

  object CheckStyleSettings {

    val checkStyle = TaskKey[Unit]("checkstyle", "run CheckStyle")
    val checkStyleTask = checkStyle <<=
      (streams, baseDirectory, sourceDirectory in Compile, target) map {
        (streams, base, src, target) =>
        import com.puppycrawl.tools.checkstyle.Main.{ main => CsMain }
        import streams.log

        val args = List(
          "-c", (base / "checkstyle-config.xml").getAbsolutePath,
          "-f", "xml",
          "-o", (target / "checkstyle-result.xml").getAbsolutePath,
          src.getAbsolutePath
        )
        log info ("using checkstyle args " + args)
        trappingExits {
          CsMain(args.toArray:_*)
        }
      }

    val all = Seq(checkStyleTask)
  }

  object PmdSettings {

    val pmd = TaskKey[Unit]("pmd", "run PMD")
    val pmdTask = pmd <<=
      (streams, baseDirectory, sourceDirectory in Compile, target) map {
        (streams, base, src, target) =>
        import net.sourceforge.pmd.PMD.{ main => PmdMain }
        import streams.log

        val args = List(
          "-dir", src.getAbsolutePath,
          "-format", "xml",
          "-rulesets", (base / "pmd-ruleset.xml").getAbsolutePath,
          "-reportfile", (target / "pmd.xml").getAbsolutePath
        )
        log info ("using pmd args " + args)
        trappingExits {
          PmdMain(args.toArray)
        }
      }

    val all = Seq(pmdTask)
  }

  def trappingExits(thunk: => Unit): Unit = {
    val originalSecManager = System.getSecurityManager
    case class NoExitsException() extends SecurityException
    System setSecurityManager new SecurityManager() {
      import java.security.Permission
      override def checkPermission(perm: Permission) {
        if (perm.getName startsWith "exitVM") throw NoExitsException()
      }
    }
    try {
      thunk
    } catch {
      case _: NoExitsException =>
      case e : Throwable => throw e
    } finally {
      System setSecurityManager originalSecManager
    }
  }
}
