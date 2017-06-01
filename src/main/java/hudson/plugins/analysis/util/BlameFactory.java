package hudson.plugins.analysis.util;

import hudson.FilePath;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.git.GitSCM;
import hudson.scm.SCM;

/**
 * Selects a matching SCM blamer for the specified job.
 *
 * @author Lukas Krose
 */
public class BlameFactory {
    /**
     * Selects a matching SCM blamer for the specified job.
     *
     * @param run       the run to get the SCM from
     * @param workspace the path to the workspace
     * @param logger    the plugin logger
     * @return the blamer
     */
    public static BlameInterface createBlamer(Run<?, ?> run, FilePath workspace, PluginLogger logger) {
        Job<?, ?> job = run.getParent();
        if (job instanceof AbstractProject) { // pipelines do not have an SCM link
            AbstractProject project = (AbstractProject) job;
            SCM scm = project.getScm();
            if (scm instanceof GitSCM) {
                logger.log("Found a git client");
                return new GitBlamer(run, workspace, logger);
            }
        }
        return new NullBlamer();
    }
}
