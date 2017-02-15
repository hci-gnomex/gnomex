package hci.gnomex.utility;

import org.hibernate.Session;
import java.util.*;

/**
 * Created by u6008750 on 2/7/2017.
 */
public class Workflow {

    private WorkflowSteps currentStep;
    public enum WorkflowSteps {
        SUBMITTED,
        SAMPLEQC,
        LIBRARYPREP,
        LIBRARYPREPQC,
        READYTOSEQUENCE,
        SEQUENCEINPROGRESS,
        DATAAVAILABLE,
        LABELING,
        HYBRIDIZATION,
        EXTRACTION;

        private String stepName;
        private int stepNum;

        public Integer getNumSamples() {
            return numSamples;
        }

        public void setNumSamples(Integer numSamples) {
            this.numSamples = numSamples;
        }

        private Integer numSamples;

        WorkflowSteps(){

        }
        WorkflowSteps(String name){

            stepName = name;
        }

        public void setStepName( String name) {
            stepName = name;
        }

        public String getStepName() {
            return stepName;
        }

        @Override
        public String toString() {
            return stepName;
        }
    }

    public static List<WorkflowSteps> getIlluminaPrepWorkflow(String codeRequestCategory, Session sess) {
        List list =  new ArrayList<WorkflowSteps>();
        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.SAMPLEQC);
        list.add(WorkflowSteps.LIBRARYPREP);
        list.add(WorkflowSteps.LIBRARYPREPQC);
        list.add(WorkflowSteps.READYTOSEQUENCE);
        list.add(WorkflowSteps.SEQUENCEINPROGRESS);
        list.add(WorkflowSteps.DATAAVAILABLE);
        getPropertyDescription(list, codeRequestCategory, sess);
        return list;
    }

    public static List<WorkflowSteps> getIlluminaPrepWorkflow() {
        List list = new ArrayList<WorkflowSteps>();

        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.SAMPLEQC);
        list.add(WorkflowSteps.LIBRARYPREP);
        list.add(WorkflowSteps.LIBRARYPREPQC);
        list.add(WorkflowSteps.READYTOSEQUENCE);
        list.add(WorkflowSteps.SEQUENCEINPROGRESS);
        list.add(WorkflowSteps.DATAAVAILABLE);

        return list;

    }
    public static List<WorkflowSteps> getIlluminaNoPrepWorkflow(String codeRequestCategory, Session sess) {
        List list =  new ArrayList<WorkflowSteps>();

        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.LIBRARYPREPQC);
        list.add(WorkflowSteps.READYTOSEQUENCE);
        list.add(WorkflowSteps.SEQUENCEINPROGRESS);
        list.add(WorkflowSteps.DATAAVAILABLE);
        getPropertyDescription(list, codeRequestCategory, sess);
        return list;
    }

    public static List<WorkflowSteps> getIlluminaNoPrepWorkflow() {

        List list =  new ArrayList<WorkflowSteps>();

        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.LIBRARYPREPQC);
        list.add(WorkflowSteps.READYTOSEQUENCE);
        list.add(WorkflowSteps.SEQUENCEINPROGRESS);
        list.add(WorkflowSteps.DATAAVAILABLE);
        return list;
    }
    public static List<WorkflowSteps> getMicroArrayWorkflow(String codeRequestCategory, Session sess) {

        List list =  new ArrayList<WorkflowSteps>();

        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.SAMPLEQC);
        list.add(WorkflowSteps.LABELING);
        list.add(WorkflowSteps.HYBRIDIZATION);
        list.add(WorkflowSteps.EXTRACTION);
        getPropertyDescription(list, codeRequestCategory, sess);
        return list;
    }

    public static List<WorkflowSteps> getMicroArrayWorkflow() {

        List list =  new ArrayList<WorkflowSteps>();

        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.SAMPLEQC);
        list.add(WorkflowSteps.LABELING);
        list.add(WorkflowSteps.HYBRIDIZATION);
        list.add(WorkflowSteps.EXTRACTION);
        return list;

    }

    public static List<WorkflowSteps> getQualityControlWorkflow(String codeRequestCategory, Session sess) {

        List list =  new ArrayList<WorkflowSteps>();

        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.SAMPLEQC);
        getPropertyDescription(list, codeRequestCategory, sess);
        return list;
    }

    public static List<WorkflowSteps> getQualityControlWorkflow() {

        List list =  new ArrayList<WorkflowSteps>();

        list.add(WorkflowSteps.SUBMITTED);
        list.add(WorkflowSteps.SAMPLEQC);
        return list;
    }

    private static void getPropertyDescription(List list, String codeRequestCategory, Session sess) {
        WorkflowPropertyHelper workflowPropertyHelper = WorkflowPropertyHelper.getInstance(sess);

        Iterator iterator = list.iterator();

        for(int i=0 ; i < list.size() ; i++ ) {
            WorkflowSteps workflowSteps = ((WorkflowSteps)iterator.next());
            String property = workflowPropertyHelper.getRequestCategoryProperty(workflowSteps.name(), codeRequestCategory);
            if (property != null ) {
                workflowSteps.setStepName(property);
            }
        }
    }

}
