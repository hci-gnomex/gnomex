package hci.gnomex.daemon.auto_import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class CollaboratorPermission {
    private Map<String, List<PersonEntry>> newSampleList;
    private Integer analysisID;
    private static Map<String, List<String>> diseaseAliasMap = new TreeMap();
    private Query query;
    private static Set<String> levelOptions;
    private String level;
    private static Set<String> dataVendorOptions;
    private List<String> dataVendors = new ArrayList();
    private String attributeType;
    private List<String> attributeIDs = new ArrayList();
    public List<String> IRBs = new ArrayList();
    private static int AVATAR_FOLDER_ID = 11;
    private static int FOUNDATION_FOLDER_ID = 14;

    static {
        diseaseAliasMap.put("Abdomen", Arrays.asList("Abdominal wall"));
        diseaseAliasMap.put("Adrenal Gland", Arrays.asList());
        diseaseAliasMap.put("Anus", Arrays.asList());
        diseaseAliasMap.put("Appendix", Arrays.asList());
        diseaseAliasMap.put("Bladder", Arrays.asList());
        diseaseAliasMap.put("Blood", Arrays.asList());
        diseaseAliasMap.put("Bone", Arrays.asList("Bone Marrow", "Iliac crest", "Pelvis"));
        diseaseAliasMap.put("Brain", Arrays.asList("Neurology"));
        diseaseAliasMap.put("Breast", Arrays.asList());
        diseaseAliasMap.put("Cervix", Arrays.asList());
        diseaseAliasMap.put("Chest Wall", Arrays.asList());
        diseaseAliasMap.put("ColoCare", Arrays.asList());
        diseaseAliasMap.put("Colon", Arrays.asList("Colon Bronner"));
        diseaseAliasMap.put("Diaphragm", Arrays.asList());
        diseaseAliasMap.put("Duodenum", Arrays.asList());
        diseaseAliasMap.put("Esophagus", Arrays.asList());
        diseaseAliasMap.put("Gallbladder", Arrays.asList());
        diseaseAliasMap.put("Gastro-esophageal junction", Arrays.asList());
        diseaseAliasMap.put("Gyn", Arrays.asList());
        diseaseAliasMap.put("Head and Neck", Arrays.asList("Head Or Neck", "Eye", "Ear", "Mouth", "Nasal Cavity", "Nasopharynx And Paranasal Sinuses", "Tongue", "Trachea", "Salivary Gland"));
        diseaseAliasMap.put("Heart", Arrays.asList());
        diseaseAliasMap.put("HEM", Arrays.asList("HEM-CLL", "HEM-CML", "HEM-MM"));
        diseaseAliasMap.put("Kidney", Arrays.asList("CG - Kidney", "Ureter"));
        diseaseAliasMap.put("Liver", Arrays.asList());
        diseaseAliasMap.put("Lung", Arrays.asList());
        diseaseAliasMap.put("Lymph Node", Arrays.asList());
        diseaseAliasMap.put("Mediastinum", Arrays.asList());
        diseaseAliasMap.put("Muscle", Arrays.asList());
        diseaseAliasMap.put("Omentum", Arrays.asList());
        diseaseAliasMap.put("Other", Arrays.asList());
        diseaseAliasMap.put("Pancreas", Arrays.asList());
        diseaseAliasMap.put("Parotid Gland", Arrays.asList());
        diseaseAliasMap.put("Penis", Arrays.asList());
        diseaseAliasMap.put("Peritoneum", Arrays.asList("Peritoneal Fluid"));
        diseaseAliasMap.put("Pleura", Arrays.asList("Pleura Fluid"));
        diseaseAliasMap.put("Prostate", Arrays.asList());
        diseaseAliasMap.put("Rectum", Arrays.asList());
        diseaseAliasMap.put("Retroperitoneum", Arrays.asList());
        diseaseAliasMap.put("Skin", Arrays.asList());
        diseaseAliasMap.put("Small Intestine", Arrays.asList());
        diseaseAliasMap.put("Soft Tissue", Arrays.asList());
        diseaseAliasMap.put("Spine", Arrays.asList());
        diseaseAliasMap.put("Stomach", Arrays.asList());
        diseaseAliasMap.put("Testis", Arrays.asList());
        diseaseAliasMap.put("Thymus", Arrays.asList());
        diseaseAliasMap.put("Thyroid", Arrays.asList("Thyroid Gland"));
        diseaseAliasMap.put("Urethra", Arrays.asList());
        diseaseAliasMap.put("Uterus", Arrays.asList());
        diseaseAliasMap.put("Vagina", Arrays.asList());
        diseaseAliasMap.put("Whipple Resection", Arrays.asList());
        levelOptions = new HashSet();
        levelOptions.add("experiment");
        levelOptions.add("experimentproperty");
        levelOptions.add("sampleproperty");
        levelOptions.add("sample");
        levelOptions.add("analysis");
        dataVendorOptions = new HashSet();
        dataVendorOptions.add("avatar");
        dataVendorOptions.add("foundation");
        dataVendorOptions.add("tempus");
        dataVendorOptions.add("all");
    }


    CollaboratorPermission(String[] args) throws Exception {
        for(int i = 0; i < args.length; ++i) {
            args[i] = args[i].toLowerCase();
            if (args[i].equals("-irb")) {
                ++i;
                this.parseArgs(i, this.IRBs, args);
            } else if (args[i].equals("-attributetype")) {
                List<String> tempAtypeList = new ArrayList();
                ++i;
                this.parseArgs(i, tempAtypeList, args);
                this.attributeType = String.join(" ", tempAtypeList);
                if (this.attributeType == null) {
                    throw new Exception("attributeType  is not found, this is the column name in your table ");
                }
            } else if (args[i].equals("-attributeid")) {
                ++i;
                this.parseArgs(i, this.attributeIDs, args);
            } else if (args[i].equals("-level")) {
                ++i;
                boolean isLevel = levelOptions.contains(args[i].toLowerCase());
                if (!isLevel) {
                    throw new Exception("Level is not found, the follow options for it are: experiment, sample or analysis ");
                }

                this.level = args[i].toLowerCase();
            } else if (args[i].equals("-datavendor")) {
                ++i;
                this.parseArgs(i, this.dataVendors, args);
                Iterator var3 = this.dataVendors.iterator();

                while(var3.hasNext()) {
                    String v = (String)var3.next();
                    boolean isVendor = dataVendorOptions.contains(v.toLowerCase());
                    if (!isVendor) {
                        throw new Exception("dataVendor is not found, please provide a valid option: Avatar,Foundation,Tempus, or all");
                    }
                }
            } else if (args[i].equals("-dbcredentials")) {
                ++i;
                this.query = new Query(args[i]);
            }
        }

    }

    public static void main(String[] args) {
        CollaboratorPermission cp = null;

        try {
            cp = new CollaboratorPermission(args);
            List<Integer> analysisList = cp.getAnalysesWithCriteria();
            System.out.println("Analyses to be used ");
            System.out.println(analysisList);
            Map<Integer, List<Integer>> analysisForCollabs = cp.query.getCollaboratorsForIRB(cp.IRBs, analysisList);
            cp.assignAnalysisToCollabs(analysisForCollabs);
        } catch (Exception e) {
            e.printStackTrace();
            cp.query.closeConnection();
            System.exit(1);
        } finally {
            if (cp.query != null) {
                cp.query.closeConnection();
            }

        }

    }

    private List<Integer> getAnalysesWithCriteria() {
        List<Integer> analysisIDList = new ArrayList();
        if (this.level.equals("sampleproperty")) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("SELECT summary.idAnalysis\n ");
            strBuilder.append("FROM ( SELECT s.name, s.idSample, a.idAnalysis\n ");
            strBuilder.append("FROM Sample s JOIN Request r ON s.idRequest = r.idRequest\n ");
            strBuilder.append("JOIN Analysis a ON a.name = r.name ");
            strBuilder.append(this.makeAnalysisGroupINstatement(this.dataVendors));
            strBuilder.append("\n ");
            strBuilder.append(this.makeQueryStatement(this.dataVendors));
            strBuilder.append("\n ");
            strBuilder.append(") as summary\n ");
            strBuilder.append("JOIN PropertyEntry pe ON pe.idSample = summary.idSample\n ");
            strBuilder.append("JOIN Property p ON p.idProperty = pe.idProperty\n ");
            strBuilder.append("WHERE p.name = '");
            strBuilder.append(this.attributeType);
            strBuilder.append("' AND ");
            strBuilder.append("pe.valueString LIKE ?");
            this.query.executeAnalysisWithCriteriaQuery(strBuilder.toString(), this.attributeIDs);
            System.out.println(this.attributeIDs.toString());
            System.out.println("The query: \n" + strBuilder.toString());
        } else if (!this.level.equals("sample") && !this.level.equals("experimentproperty") && !this.level.equals("experiment") && this.level.equals("analysis") && this.attributeType.equals("idanalysis")) {
            List<Integer> tempIntegerList = new ArrayList();
            for(String id: this.attributeIDs) {
                tempIntegerList.add(new Integer(id));
            }

            analysisIDList = tempIntegerList;
        }

        return analysisIDList;
    }

    private String makeQueryStatement(List<String> dataVendors) {
        StringBuilder strBuild = new StringBuilder("WHERE ");

        for(int i = 0; i < dataVendors.size(); ++i) {
            if ((dataVendors.get(i)).equals("avatar")) {
                strBuild.append(" s.name LIKE '%SL%' ");
            } else if ((dataVendors.get(i)).equals("foundation")) {
                strBuild.append(" s.name  LIKE '%RF%' ");
            } else if (!(dataVendors.get(i)).equals("tempus") && (dataVendors.get(i)).equals("all")) {
                return "";
            }

            if (i < dataVendors.size() - 1) {
                strBuild.append(" OR ");
            }
        }

        return strBuild.toString();
    }

    private String makeAnalysisGroupINstatement(List<String> dataVendors) {
        StringBuilder strBuild = new StringBuilder("JOIN AnalysisGroupItem agi ON agi.idAnalysis = a.idAnalysis AND agi.idAnalysisGroup IN (");

        for(int i = 0; i < dataVendors.size(); ++i) {
            if (((String)dataVendors.get(i)).equals("avatar")) {
                strBuild.append(AVATAR_FOLDER_ID);
            } else if (((String)dataVendors.get(i)).equals("foundation")) {
                strBuild.append(FOUNDATION_FOLDER_ID);
            } else if (!((String)dataVendors.get(i)).equals("tempus") && ((String)dataVendors.get(i)).equals("all")) {
                return "";
            }

            if (i < dataVendors.size() - 1) {
                strBuild.append(", ");
            }
        }

        strBuild.append(")");
        return strBuild.toString();
    }

    public List<String> getIRAAssociation() throws Exception {
        ArrayList<String> associations = new ArrayList<>();

        for(String pKey : this.newSampleList.keySet()){
            List<PersonEntry> pEntries = this.newSampleList.get(pKey);
            for(PersonEntry pEntry : pEntries){
                // determine if avatar or foundation,  method name lies it could be either
                String queryStr = "";
                String targetStr = "";
                if (pEntry.getSlNumber().contains("SL")) {
                    queryStr = pEntry.getSubmittedDiagnosis();
                } else {
                    queryStr = pEntry.getTissueType();
                }

                System.out.println("Query String is: " + queryStr);
                if (diseaseAliasMap.get(queryStr) != null) {
                    targetStr = queryStr;
                } else {
                    String startWord = queryStr.split(" ")[0];
                    if (diseaseAliasMap.get(startWord) != null) {
                        targetStr = startWord;
                    } else {
                        targetStr = searchAllAliases(queryStr);
                    }
                }

                if (targetStr.equals("")) {
                    throw new Exception("The disease group could not be found with the query string: " + queryStr);
                }

                associations.add(targetStr);
                System.out.println("The target string is: " + targetStr);
            }
        }

        return associations;
    }

    private String searchAllAliases(String queryStr) {
        String targetStr = "";
        String lowerQueryStr = queryStr.toLowerCase();
        boolean found = false;

        for(String key : diseaseAliasMap.keySet()){
            List<String> aliasGroup = diseaseAliasMap.get(key);
            for(String alias :  aliasGroup){
                if (alias.toLowerCase().equals(lowerQueryStr)) {
                    targetStr = key;
                    found = true;
                    break;
                }
            }

            if (found) {
                break;
            }
        }

        return targetStr;
    }

    public void assignAnalysisToCollabs(Map<Integer, List<Integer>> collabsForAnalysis) {
        Iterator it = collabsForAnalysis.entrySet().iterator();

        while(it.hasNext()) {
            Entry<Integer, List<Integer>> entry = (Entry)it.next();
            Integer aKey = (Integer)entry.getKey();
            List<Integer> collabs = (List)collabsForAnalysis.get(aKey);
            this.query.assignPermissions(collabs, aKey);
        }

    }

    public void assignAnalysisPermissionToCollabs(List<String> irbAssocationList) {
    }

    void parseArgs(int startIndex, List<String> itemList, String[] args) {
        for(int i = startIndex; i < args.length; ++i) {
            String item = args[i];
            if (item.charAt(0) == '-') {
                break;
            }

            itemList.add(item.toLowerCase());
        }

    }


}
