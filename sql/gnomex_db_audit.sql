
USE gnomex;

delimiter $$

DROP TRIGGER IF EXISTS TrAI_AlignmentPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAU_AlignmentPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAD_AlignmentPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAI_AlignmentProfile_FER
$$
DROP TRIGGER IF EXISTS TrAU_AlignmentProfile_FER
$$
DROP TRIGGER IF EXISTS TrAD_AlignmentProfile_FER
$$
DROP TRIGGER IF EXISTS TrAI_AlignmentProfileGenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAU_AlignmentProfileGenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAD_AlignmentProfileGenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAI_Analysis_FER
$$
DROP TRIGGER IF EXISTS TrAU_Analysis_FER
$$
DROP TRIGGER IF EXISTS TrAD_Analysis_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisExperimentItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisExperimentItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisExperimentItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisGenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisGenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisGenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisGroup_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisGroup_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisGroup_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisGroupItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisGroupItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisGroupItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnnotationReportField_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnnotationReportField_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnnotationReportField_FER
$$
DROP TRIGGER IF EXISTS TrAI_Application_FER
$$
DROP TRIGGER IF EXISTS TrAU_Application_FER
$$
DROP TRIGGER IF EXISTS TrAD_Application_FER
$$
DROP TRIGGER IF EXISTS TrAI_ApplicationTheme_FER
$$
DROP TRIGGER IF EXISTS TrAU_ApplicationTheme_FER
$$
DROP TRIGGER IF EXISTS TrAD_ApplicationTheme_FER
$$
DROP TRIGGER IF EXISTS TrAI_ApplicationType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ApplicationType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ApplicationType_FER
$$
DROP TRIGGER IF EXISTS TrAI_AppUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_AppUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_AppUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_ArrayCoordinate_FER
$$
DROP TRIGGER IF EXISTS TrAU_ArrayCoordinate_FER
$$
DROP TRIGGER IF EXISTS TrAD_ArrayCoordinate_FER
$$
DROP TRIGGER IF EXISTS TrAI_ArrayDesign_FER
$$
DROP TRIGGER IF EXISTS TrAU_ArrayDesign_FER
$$
DROP TRIGGER IF EXISTS TrAD_ArrayDesign_FER
$$
DROP TRIGGER IF EXISTS TrAI_Assay_FER
$$
DROP TRIGGER IF EXISTS TrAU_Assay_FER
$$
DROP TRIGGER IF EXISTS TrAD_Assay_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingAccount_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingAccount_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingAccount_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingAccountUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingAccountUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingAccountUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingChargeKind_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingChargeKind_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingChargeKind_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingPeriod_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingPeriod_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingPeriod_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingSlideProductClass_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingSlideProductClass_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingSlideProductClass_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingSlideServiceClass_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingSlideServiceClass_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingSlideServiceClass_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingTemplate_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingTemplate_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingTemplate_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingTemplateItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingTemplateItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingTemplateItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_BioanalyzerChipType_FER
$$
DROP TRIGGER IF EXISTS TrAU_BioanalyzerChipType_FER
$$
DROP TRIGGER IF EXISTS TrAD_BioanalyzerChipType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Chromatogram_FER
$$
DROP TRIGGER IF EXISTS TrAU_Chromatogram_FER
$$
DROP TRIGGER IF EXISTS TrAD_Chromatogram_FER
$$
DROP TRIGGER IF EXISTS TrAI_ConcentrationUnit_FER
$$
DROP TRIGGER IF EXISTS TrAU_ConcentrationUnit_FER
$$
DROP TRIGGER IF EXISTS TrAD_ConcentrationUnit_FER
$$
DROP TRIGGER IF EXISTS TrAI_ContextSensitiveHelp_FER
$$
DROP TRIGGER IF EXISTS TrAU_ContextSensitiveHelp_FER
$$
DROP TRIGGER IF EXISTS TrAD_ContextSensitiveHelp_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacility_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacility_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacility_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacilityLab_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacilityLab_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacilityLab_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacilityManager_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacilityManager_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacilityManager_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacilitySubmitter_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacilitySubmitter_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacilitySubmitter_FER
$$
DROP TRIGGER IF EXISTS TrAI_CreditCardCompany_FER
$$
DROP TRIGGER IF EXISTS TrAU_CreditCardCompany_FER
$$
DROP TRIGGER IF EXISTS TrAD_CreditCardCompany_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackFolder_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackFolder_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackFolder_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackToFolder_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackToFolder_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackToFolder_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAI_DiskUsageByMonth_FER
$$
DROP TRIGGER IF EXISTS TrAU_DiskUsageByMonth_FER
$$
DROP TRIGGER IF EXISTS TrAD_DiskUsageByMonth_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentDesign_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentDesign_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentDesign_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentDesignEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentDesignEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentDesignEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentFactor_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentFactor_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentFactor_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentFactorEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentFactorEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentFactorEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_FAQ_FER
$$
DROP TRIGGER IF EXISTS TrAU_FAQ_FER
$$
DROP TRIGGER IF EXISTS TrAD_FAQ_FER
$$
DROP TRIGGER IF EXISTS TrAI_FeatureExtractionProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_FeatureExtractionProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_FeatureExtractionProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_FlowCell_FER
$$
DROP TRIGGER IF EXISTS TrAU_FlowCell_FER
$$
DROP TRIGGER IF EXISTS TrAD_FlowCell_FER
$$
DROP TRIGGER IF EXISTS TrAI_FlowCellChannel_FER
$$
DROP TRIGGER IF EXISTS TrAU_FlowCellChannel_FER
$$
DROP TRIGGER IF EXISTS TrAD_FlowCellChannel_FER
$$
DROP TRIGGER IF EXISTS TrAI_FundingAgency_FER
$$
DROP TRIGGER IF EXISTS TrAU_FundingAgency_FER
$$
DROP TRIGGER IF EXISTS TrAD_FundingAgency_FER
$$
DROP TRIGGER IF EXISTS TrAI_GenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAU_GenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAD_GenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAI_GenomeBuildAlias_FER
$$
DROP TRIGGER IF EXISTS TrAU_GenomeBuildAlias_FER
$$
DROP TRIGGER IF EXISTS TrAD_GenomeBuildAlias_FER
$$
DROP TRIGGER IF EXISTS TrAI_GenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAU_GenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAD_GenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAI_HybProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_HybProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_HybProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_Hybridization_FER
$$
DROP TRIGGER IF EXISTS TrAU_Hybridization_FER
$$
DROP TRIGGER IF EXISTS TrAD_Hybridization_FER
$$
DROP TRIGGER IF EXISTS TrAI_Institution_FER
$$
DROP TRIGGER IF EXISTS TrAU_Institution_FER
$$
DROP TRIGGER IF EXISTS TrAD_Institution_FER
$$
DROP TRIGGER IF EXISTS TrAI_InstitutionLab_FER
$$
DROP TRIGGER IF EXISTS TrAU_InstitutionLab_FER
$$
DROP TRIGGER IF EXISTS TrAD_InstitutionLab_FER
$$
DROP TRIGGER IF EXISTS TrAI_Instrument_FER
$$
DROP TRIGGER IF EXISTS TrAU_Instrument_FER
$$
DROP TRIGGER IF EXISTS TrAD_Instrument_FER
$$
DROP TRIGGER IF EXISTS TrAI_InstrumentRun_FER
$$
DROP TRIGGER IF EXISTS TrAU_InstrumentRun_FER
$$
DROP TRIGGER IF EXISTS TrAD_InstrumentRun_FER
$$
DROP TRIGGER IF EXISTS TrAI_InstrumentRunStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_InstrumentRunStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_InstrumentRunStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_InternalAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAU_InternalAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAD_InternalAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAI_Invoice_FER
$$
DROP TRIGGER IF EXISTS TrAU_Invoice_FER
$$
DROP TRIGGER IF EXISTS TrAD_Invoice_FER
$$
DROP TRIGGER IF EXISTS TrAI_IsolationPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAU_IsolationPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAD_IsolationPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Lab_FER
$$
DROP TRIGGER IF EXISTS TrAU_Lab_FER
$$
DROP TRIGGER IF EXISTS TrAD_Lab_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_Label_FER
$$
DROP TRIGGER IF EXISTS TrAU_Label_FER
$$
DROP TRIGGER IF EXISTS TrAD_Label_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabeledSample_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabeledSample_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabeledSample_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabelingProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabelingProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabelingProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabelingReactionSize_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabelingReactionSize_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabelingReactionSize_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabManager_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabManager_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabManager_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_LibraryPrepQCProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_LibraryPrepQCProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_LibraryPrepQCProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_MasterBillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_MasterBillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_MasterBillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAU_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAD_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAI_MicroArrayRequestNumber_FER
$$
DROP TRIGGER IF EXISTS TrAU_MicroArrayRequestNumber_FER
$$
DROP TRIGGER IF EXISTS TrAD_MicroArrayRequestNumber_FER
$$
DROP TRIGGER IF EXISTS TrAI_NewsItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_NewsItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_NewsItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_Notification_FER
$$
DROP TRIGGER IF EXISTS TrAU_Notification_FER
$$
DROP TRIGGER IF EXISTS TrAD_Notification_FER
$$
DROP TRIGGER IF EXISTS TrAI_NucleotideType_FER
$$
DROP TRIGGER IF EXISTS TrAU_NucleotideType_FER
$$
DROP TRIGGER IF EXISTS TrAD_NucleotideType_FER
$$
DROP TRIGGER IF EXISTS TrAI_NumberSequencingCycles_FER
$$
DROP TRIGGER IF EXISTS TrAU_NumberSequencingCycles_FER
$$
DROP TRIGGER IF EXISTS TrAD_NumberSequencingCycles_FER
$$
DROP TRIGGER IF EXISTS TrAI_NumberSequencingCyclesAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAU_NumberSequencingCyclesAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAD_NumberSequencingCyclesAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAI_OligoBarcode_FER
$$
DROP TRIGGER IF EXISTS TrAU_OligoBarcode_FER
$$
DROP TRIGGER IF EXISTS TrAD_OligoBarcode_FER
$$
DROP TRIGGER IF EXISTS TrAI_OligoBarcodeScheme_FER
$$
DROP TRIGGER IF EXISTS TrAU_OligoBarcodeScheme_FER
$$
DROP TRIGGER IF EXISTS TrAD_OligoBarcodeScheme_FER
$$
DROP TRIGGER IF EXISTS TrAI_OligoBarcodeSchemeAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAU_OligoBarcodeSchemeAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAD_OligoBarcodeSchemeAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAI_Organism_FER
$$
DROP TRIGGER IF EXISTS TrAU_Organism_FER
$$
DROP TRIGGER IF EXISTS TrAD_Organism_FER
$$
DROP TRIGGER IF EXISTS TrAI_OtherAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAU_OtherAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAD_OtherAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAI_Plate_FER
$$
DROP TRIGGER IF EXISTS TrAU_Plate_FER
$$
DROP TRIGGER IF EXISTS TrAD_Plate_FER
$$
DROP TRIGGER IF EXISTS TrAI_PlateType_FER
$$
DROP TRIGGER IF EXISTS TrAU_PlateType_FER
$$
DROP TRIGGER IF EXISTS TrAD_PlateType_FER
$$
DROP TRIGGER IF EXISTS TrAI_PlateWell_FER
$$
DROP TRIGGER IF EXISTS TrAU_PlateWell_FER
$$
DROP TRIGGER IF EXISTS TrAD_PlateWell_FER
$$
DROP TRIGGER IF EXISTS TrAI_Price_FER
$$
DROP TRIGGER IF EXISTS TrAU_Price_FER
$$
DROP TRIGGER IF EXISTS TrAD_Price_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceCategoryStep_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceCategoryStep_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceCategoryStep_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceCriteria_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceCriteria_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceCriteria_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceSheet_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceSheet_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceSheet_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceSheetPriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceSheetPriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceSheetPriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceSheetRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceSheetRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceSheetRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_Primer_FER
$$
DROP TRIGGER IF EXISTS TrAU_Primer_FER
$$
DROP TRIGGER IF EXISTS TrAD_Primer_FER
$$
DROP TRIGGER IF EXISTS TrAI_Product_FER
$$
DROP TRIGGER IF EXISTS TrAU_Product_FER
$$
DROP TRIGGER IF EXISTS TrAD_Product_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductLedger_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductLedger_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductLedger_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductLineItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductLineItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductLineItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductOrder_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductOrder_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductOrder_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductOrderFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductOrderFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductOrderFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductOrderStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductOrderStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductOrderStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Project_FER
$$
DROP TRIGGER IF EXISTS TrAU_Project_FER
$$
DROP TRIGGER IF EXISTS TrAD_Project_FER
$$
DROP TRIGGER IF EXISTS TrAI_Property_FER
$$
DROP TRIGGER IF EXISTS TrAU_Property_FER
$$
DROP TRIGGER IF EXISTS TrAD_Property_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyAnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyAnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyAnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyAppUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyAppUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyAppUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyDictionary_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyDictionary_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyDictionary_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyEntryOption_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyEntryOption_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyEntryOption_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyEntryValue_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyEntryValue_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyEntryValue_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyOption_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyOption_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyOption_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyOrganism_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyOrganism_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyOrganism_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyPlatformApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyPlatformApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyPlatformApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyType_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyType_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyType_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProtocolType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProtocolType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProtocolType_FER
$$
DROP TRIGGER IF EXISTS TrAI_QualityControlStep_FER
$$
DROP TRIGGER IF EXISTS TrAU_QualityControlStep_FER
$$
DROP TRIGGER IF EXISTS TrAD_QualityControlStep_FER
$$
DROP TRIGGER IF EXISTS TrAI_QualityControlStepEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_QualityControlStepEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_QualityControlStepEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_ReactionType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ReactionType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ReactionType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Request_FER
$$
DROP TRIGGER IF EXISTS TrAU_Request_FER
$$
DROP TRIGGER IF EXISTS TrAD_Request_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCategoryApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCategoryApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCategoryApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCategoryType_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCategoryType_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCategoryType_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestHybridization_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestHybridization_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestHybridization_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestSeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestSeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestSeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAI_Sample_FER
$$
DROP TRIGGER IF EXISTS TrAU_Sample_FER
$$
DROP TRIGGER IF EXISTS TrAD_Sample_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleDropOffLocation_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleDropOffLocation_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleDropOffLocation_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleFileType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleFileType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleFileType_FER
$$
DROP TRIGGER IF EXISTS TrAI_SamplePrepMethod_FER
$$
DROP TRIGGER IF EXISTS TrAU_SamplePrepMethod_FER
$$
DROP TRIGGER IF EXISTS TrAD_SamplePrepMethod_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleSource_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleSource_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleSource_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleType_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleTypeRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleTypeRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleTypeRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_ScanProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_ScanProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_ScanProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_SealType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SealType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SealType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Segment_FER
$$
DROP TRIGGER IF EXISTS TrAU_Segment_FER
$$
DROP TRIGGER IF EXISTS TrAD_Segment_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqLibProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqLibProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqLibProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqLibProtocolApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqLibProtocolApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqLibProtocolApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqRunType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqRunType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqRunType_FER
$$
DROP TRIGGER IF EXISTS TrAI_SequenceLane_FER
$$
DROP TRIGGER IF EXISTS TrAU_SequenceLane_FER
$$
DROP TRIGGER IF EXISTS TrAD_SequenceLane_FER
$$
DROP TRIGGER IF EXISTS TrAI_SequencingControl_FER
$$
DROP TRIGGER IF EXISTS TrAU_SequencingControl_FER
$$
DROP TRIGGER IF EXISTS TrAD_SequencingControl_FER
$$
DROP TRIGGER IF EXISTS TrAI_SequencingPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAU_SequencingPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAD_SequencingPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAI_Slide_FER
$$
DROP TRIGGER IF EXISTS TrAU_Slide_FER
$$
DROP TRIGGER IF EXISTS TrAD_Slide_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideDesign_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideDesign_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideDesign_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideProduct_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideProduct_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideProduct_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideProductApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideProductApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideProductApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideSource_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideSource_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideSource_FER
$$
DROP TRIGGER IF EXISTS TrAI_State_FER
$$
DROP TRIGGER IF EXISTS TrAU_State_FER
$$
DROP TRIGGER IF EXISTS TrAD_State_FER
$$
DROP TRIGGER IF EXISTS TrAI_Step_FER
$$
DROP TRIGGER IF EXISTS TrAU_Step_FER
$$
DROP TRIGGER IF EXISTS TrAD_Step_FER
$$
DROP TRIGGER IF EXISTS TrAI_SubmissionInstruction_FER
$$
DROP TRIGGER IF EXISTS TrAU_SubmissionInstruction_FER
$$
DROP TRIGGER IF EXISTS TrAD_SubmissionInstruction_FER
$$
DROP TRIGGER IF EXISTS TrAI_Topic_FER
$$
DROP TRIGGER IF EXISTS TrAU_Topic_FER
$$
DROP TRIGGER IF EXISTS TrAD_Topic_FER
$$
DROP TRIGGER IF EXISTS TrAI_TreatmentEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_TreatmentEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_TreatmentEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_UnloadDataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAU_UnloadDataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAD_UnloadDataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAI_UserPermissionKind_FER
$$
DROP TRIGGER IF EXISTS TrAU_UserPermissionKind_FER
$$
DROP TRIGGER IF EXISTS TrAD_UserPermissionKind_FER
$$
DROP TRIGGER IF EXISTS TrAI_Vendor_FER
$$
DROP TRIGGER IF EXISTS TrAU_Vendor_FER
$$
DROP TRIGGER IF EXISTS TrAD_Vendor_FER
$$
DROP TRIGGER IF EXISTS TrAI_Visibility_FER
$$
DROP TRIGGER IF EXISTS TrAU_Visibility_FER
$$
DROP TRIGGER IF EXISTS TrAD_Visibility_FER
$$
DROP TRIGGER IF EXISTS TrAI_WorkItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_WorkItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_WorkItem_FER
$$


--
-- Audit Table For AlignmentPlatform 
--

-- select 'Creating table AlignmentPlatform'$$

-- DROP TABLE IF EXISTS `AlignmentPlatform_Audit`$$

CREATE TABLE IF NOT EXISTS `AlignmentPlatform_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`alignmentPlatformName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AlignmentPlatform 
--

INSERT INTO AlignmentPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive
  FROM AlignmentPlatform
  WHERE NOT EXISTS(SELECT * FROM AlignmentPlatform_Audit)
$$

--
-- Audit Triggers For AlignmentPlatform 
--


CREATE TRIGGER TrAI_AlignmentPlatform_FER AFTER INSERT ON AlignmentPlatform FOR EACH ROW
BEGIN
  INSERT INTO AlignmentPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAlignmentPlatform
  , NEW.alignmentPlatformName
  , NEW.webServiceName
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_AlignmentPlatform_FER AFTER UPDATE ON AlignmentPlatform FOR EACH ROW
BEGIN
  INSERT INTO AlignmentPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAlignmentPlatform
  , NEW.alignmentPlatformName
  , NEW.webServiceName
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_AlignmentPlatform_FER AFTER DELETE ON AlignmentPlatform FOR EACH ROW
BEGIN
  INSERT INTO AlignmentPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAlignmentPlatform
  , OLD.alignmentPlatformName
  , OLD.webServiceName
  , OLD.isActive );
END;
$$


--
-- Audit Table For AlignmentProfileGenomeIndex 
--

-- select 'Creating table AlignmentProfileGenomeIndex'$$

-- DROP TABLE IF EXISTS `AlignmentProfileGenomeIndex_Audit`$$

CREATE TABLE IF NOT EXISTS `AlignmentProfileGenomeIndex_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AlignmentProfileGenomeIndex 
--

INSERT INTO AlignmentProfileGenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , idGenomeIndex )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAlignmentProfile
  , idGenomeIndex
  FROM AlignmentProfileGenomeIndex
  WHERE NOT EXISTS(SELECT * FROM AlignmentProfileGenomeIndex_Audit)
$$

--
-- Audit Triggers For AlignmentProfileGenomeIndex 
--


CREATE TRIGGER TrAI_AlignmentProfileGenomeIndex_FER AFTER INSERT ON AlignmentProfileGenomeIndex FOR EACH ROW
BEGIN
  INSERT INTO AlignmentProfileGenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAlignmentProfile
  , NEW.idGenomeIndex );
END;
$$


CREATE TRIGGER TrAU_AlignmentProfileGenomeIndex_FER AFTER UPDATE ON AlignmentProfileGenomeIndex FOR EACH ROW
BEGIN
  INSERT INTO AlignmentProfileGenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAlignmentProfile
  , NEW.idGenomeIndex );
END;
$$


CREATE TRIGGER TrAD_AlignmentProfileGenomeIndex_FER AFTER DELETE ON AlignmentProfileGenomeIndex FOR EACH ROW
BEGIN
  INSERT INTO AlignmentProfileGenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAlignmentProfile
  , OLD.idGenomeIndex );
END;
$$


--
-- Audit Table For AlignmentProfile 
--

-- select 'Creating table AlignmentProfile'$$

-- DROP TABLE IF EXISTS `AlignmentProfile_Audit`$$

CREATE TABLE IF NOT EXISTS `AlignmentProfile_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`alignmentProfileName`  varchar(120)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`parameters`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AlignmentProfile 
--

INSERT INTO AlignmentProfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType
  FROM AlignmentProfile
  WHERE NOT EXISTS(SELECT * FROM AlignmentProfile_Audit)
$$

--
-- Audit Triggers For AlignmentProfile 
--


CREATE TRIGGER TrAI_AlignmentProfile_FER AFTER INSERT ON AlignmentProfile FOR EACH ROW
BEGIN
  INSERT INTO AlignmentProfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAlignmentProfile
  , NEW.alignmentProfileName
  , NEW.description
  , NEW.parameters
  , NEW.isActive
  , NEW.idAlignmentPlatform
  , NEW.idSeqRunType );
END;
$$


CREATE TRIGGER TrAU_AlignmentProfile_FER AFTER UPDATE ON AlignmentProfile FOR EACH ROW
BEGIN
  INSERT INTO AlignmentProfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAlignmentProfile
  , NEW.alignmentProfileName
  , NEW.description
  , NEW.parameters
  , NEW.isActive
  , NEW.idAlignmentPlatform
  , NEW.idSeqRunType );
END;
$$


CREATE TRIGGER TrAD_AlignmentProfile_FER AFTER DELETE ON AlignmentProfile FOR EACH ROW
BEGIN
  INSERT INTO AlignmentProfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAlignmentProfile
  , OLD.alignmentProfileName
  , OLD.description
  , OLD.parameters
  , OLD.isActive
  , OLD.idAlignmentPlatform
  , OLD.idSeqRunType );
END;
$$


--
-- Audit Table For AnalysisCollaborator 
--

-- select 'Creating table AnalysisCollaborator'$$

-- DROP TABLE IF EXISTS `AnalysisCollaborator_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisCollaborator 
--

INSERT INTO AnalysisCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate
  FROM AnalysisCollaborator
  WHERE NOT EXISTS(SELECT * FROM AnalysisCollaborator_Audit)
$$

--
-- Audit Triggers For AnalysisCollaborator 
--


CREATE TRIGGER TrAI_AnalysisCollaborator_FER AFTER INSERT ON AnalysisCollaborator FOR EACH ROW
BEGIN
  INSERT INTO AnalysisCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysis
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAU_AnalysisCollaborator_FER AFTER UPDATE ON AnalysisCollaborator FOR EACH ROW
BEGIN
  INSERT INTO AnalysisCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysis
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAD_AnalysisCollaborator_FER AFTER DELETE ON AnalysisCollaborator FOR EACH ROW
BEGIN
  INSERT INTO AnalysisCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysis
  , OLD.idAppUser
  , OLD.canUploadData
  , OLD.canUpdate );
END;
$$


--
-- Audit Table For AnalysisExperimentItem 
--

-- select 'Creating table AnalysisExperimentItem'$$

-- DROP TABLE IF EXISTS `AnalysisExperimentItem_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisExperimentItem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysisExperimentItem`  int(10)  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisExperimentItem 
--

INSERT INTO AnalysisExperimentItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample
  FROM AnalysisExperimentItem
  WHERE NOT EXISTS(SELECT * FROM AnalysisExperimentItem_Audit)
$$

--
-- Audit Triggers For AnalysisExperimentItem 
--


CREATE TRIGGER TrAI_AnalysisExperimentItem_FER AFTER INSERT ON AnalysisExperimentItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisExperimentItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisExperimentItem
  , NEW.idSequenceLane
  , NEW.idHybridization
  , NEW.comments
  , NEW.idAnalysis
  , NEW.idRequest
  , NEW.idSample );
END;
$$


CREATE TRIGGER TrAU_AnalysisExperimentItem_FER AFTER UPDATE ON AnalysisExperimentItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisExperimentItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisExperimentItem
  , NEW.idSequenceLane
  , NEW.idHybridization
  , NEW.comments
  , NEW.idAnalysis
  , NEW.idRequest
  , NEW.idSample );
END;
$$


CREATE TRIGGER TrAD_AnalysisExperimentItem_FER AFTER DELETE ON AnalysisExperimentItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisExperimentItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysisExperimentItem
  , OLD.idSequenceLane
  , OLD.idHybridization
  , OLD.comments
  , OLD.idAnalysis
  , OLD.idRequest
  , OLD.idSample );
END;
$$


--
-- Audit Table For AnalysisFile 
--

-- select 'Creating table AnalysisFile'$$

-- DROP TABLE IF EXISTS `AnalysisFile_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisFile_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`uploadDate`  datetime  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`baseFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisFile 
--

INSERT INTO AnalysisFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisFile
  , fileName
  , fileSize
  , comments
  , uploadDate
  , idAnalysis
  , qualifiedFilePath
  , baseFilePath
  , createDate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysisFile
  , fileName
  , fileSize
  , comments
  , uploadDate
  , idAnalysis
  , qualifiedFilePath
  , baseFilePath
  , createDate
  FROM AnalysisFile
  WHERE NOT EXISTS(SELECT * FROM AnalysisFile_Audit)
$$

--
-- Audit Triggers For AnalysisFile 
--


CREATE TRIGGER TrAI_AnalysisFile_FER AFTER INSERT ON AnalysisFile FOR EACH ROW
BEGIN
  INSERT INTO AnalysisFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisFile
  , fileName
  , fileSize
  , comments
  , uploadDate
  , idAnalysis
  , qualifiedFilePath
  , baseFilePath
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisFile
  , NEW.fileName
  , NEW.fileSize
  , NEW.comments
  , NEW.uploadDate
  , NEW.idAnalysis
  , NEW.qualifiedFilePath
  , NEW.baseFilePath
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAU_AnalysisFile_FER AFTER UPDATE ON AnalysisFile FOR EACH ROW
BEGIN
  INSERT INTO AnalysisFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisFile
  , fileName
  , fileSize
  , comments
  , uploadDate
  , idAnalysis
  , qualifiedFilePath
  , baseFilePath
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisFile
  , NEW.fileName
  , NEW.fileSize
  , NEW.comments
  , NEW.uploadDate
  , NEW.idAnalysis
  , NEW.qualifiedFilePath
  , NEW.baseFilePath
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAD_AnalysisFile_FER AFTER DELETE ON AnalysisFile FOR EACH ROW
BEGIN
  INSERT INTO AnalysisFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisFile
  , fileName
  , fileSize
  , comments
  , uploadDate
  , idAnalysis
  , qualifiedFilePath
  , baseFilePath
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysisFile
  , OLD.fileName
  , OLD.fileSize
  , OLD.comments
  , OLD.uploadDate
  , OLD.idAnalysis
  , OLD.qualifiedFilePath
  , OLD.baseFilePath
  , OLD.createDate );
END;
$$


--
-- Audit Table For AnalysisGenomebuild 
--

-- select 'Creating table AnalysisGenomebuild'$$

-- DROP TABLE IF EXISTS `AnalysisGenomebuild_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisGenomebuild_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisGenomebuild 
--

INSERT INTO AnalysisGenomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idGenomeBuild )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysis
  , idGenomeBuild
  FROM AnalysisGenomebuild
  WHERE NOT EXISTS(SELECT * FROM AnalysisGenomebuild_Audit)
$$

--
-- Audit Triggers For AnalysisGenomebuild 
--


CREATE TRIGGER TrAI_AnalysisGenomebuild_FER AFTER INSERT ON AnalysisGenomebuild FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGenomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysis
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAU_AnalysisGenomebuild_FER AFTER UPDATE ON AnalysisGenomebuild FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGenomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysis
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAD_AnalysisGenomebuild_FER AFTER DELETE ON AnalysisGenomebuild FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGenomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysis
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For AnalysisGroupitem 
--

-- select 'Creating table AnalysisGroupitem'$$

-- DROP TABLE IF EXISTS `AnalysisGroupitem_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisGroupitem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisGroupitem 
--

INSERT INTO AnalysisGroupitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , idAnalysis )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysisGroup
  , idAnalysis
  FROM AnalysisGroupitem
  WHERE NOT EXISTS(SELECT * FROM AnalysisGroupitem_Audit)
$$

--
-- Audit Triggers For AnalysisGroupitem 
--


CREATE TRIGGER TrAI_AnalysisGroupitem_FER AFTER INSERT ON AnalysisGroupitem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroupitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisGroup
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAU_AnalysisGroupitem_FER AFTER UPDATE ON AnalysisGroupitem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroupitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisGroup
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAD_AnalysisGroupitem_FER AFTER DELETE ON AnalysisGroupitem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroupitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysisGroup
  , OLD.idAnalysis );
END;
$$


--
-- Audit Table For AnalysisGroup 
--

-- select 'Creating table AnalysisGroup'$$

-- DROP TABLE IF EXISTS `AnalysisGroup_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisGroup_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisGroup 
--

INSERT INTO AnalysisGroup_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser
  FROM AnalysisGroup
  WHERE NOT EXISTS(SELECT * FROM AnalysisGroup_Audit)
$$

--
-- Audit Triggers For AnalysisGroup 
--


CREATE TRIGGER TrAI_AnalysisGroup_FER AFTER INSERT ON AnalysisGroup FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroup_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisGroup
  , NEW.name
  , NEW.description
  , NEW.idLab
  , NEW.codeVisibility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_AnalysisGroup_FER AFTER UPDATE ON AnalysisGroup FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroup_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisGroup
  , NEW.name
  , NEW.description
  , NEW.idLab
  , NEW.codeVisibility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_AnalysisGroup_FER AFTER DELETE ON AnalysisGroup FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroup_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysisGroup
  , OLD.name
  , OLD.description
  , OLD.idLab
  , OLD.codeVisibility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For AnalysisProtocol 
--

-- select 'Creating table AnalysisProtocol'$$

-- DROP TABLE IF EXISTS `AnalysisProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysisProtocol`  int(10)  NULL DEFAULT NULL
 ,`analysisProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisProtocol 
--

INSERT INTO AnalysisProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser
  FROM AnalysisProtocol
  WHERE NOT EXISTS(SELECT * FROM AnalysisProtocol_Audit)
$$

--
-- Audit Triggers For AnalysisProtocol 
--


CREATE TRIGGER TrAI_AnalysisProtocol_FER AFTER INSERT ON AnalysisProtocol FOR EACH ROW
BEGIN
  INSERT INTO AnalysisProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisProtocol
  , NEW.analysisProtocol
  , NEW.description
  , NEW.url
  , NEW.idAnalysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_AnalysisProtocol_FER AFTER UPDATE ON AnalysisProtocol FOR EACH ROW
BEGIN
  INSERT INTO AnalysisProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisProtocol
  , NEW.analysisProtocol
  , NEW.description
  , NEW.url
  , NEW.idAnalysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_AnalysisProtocol_FER AFTER DELETE ON AnalysisProtocol FOR EACH ROW
BEGIN
  INSERT INTO AnalysisProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysisProtocol
  , OLD.analysisProtocol
  , OLD.description
  , OLD.url
  , OLD.idAnalysisType
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For AnalysisToTopic 
--

-- select 'Creating table AnalysisToTopic'$$

-- DROP TABLE IF EXISTS `AnalysisToTopic_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisToTopic_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisToTopic 
--

INSERT INTO AnalysisToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idAnalysis )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idTopic
  , idAnalysis
  FROM AnalysisToTopic
  WHERE NOT EXISTS(SELECT * FROM AnalysisToTopic_Audit)
$$

--
-- Audit Triggers For AnalysisToTopic 
--


CREATE TRIGGER TrAI_AnalysisToTopic_FER AFTER INSERT ON AnalysisToTopic FOR EACH ROW
BEGIN
  INSERT INTO AnalysisToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAU_AnalysisToTopic_FER AFTER UPDATE ON AnalysisToTopic FOR EACH ROW
BEGIN
  INSERT INTO AnalysisToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAD_AnalysisToTopic_FER AFTER DELETE ON AnalysisToTopic FOR EACH ROW
BEGIN
  INSERT INTO AnalysisToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idTopic
  , OLD.idAnalysis );
END;
$$


--
-- Audit Table For AnalysisType 
--

-- select 'Creating table AnalysisType'$$

-- DROP TABLE IF EXISTS `AnalysisType_Audit`$$

CREATE TABLE IF NOT EXISTS `AnalysisType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`analysisType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnalysisType 
--

INSERT INTO AnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser
  FROM AnalysisType
  WHERE NOT EXISTS(SELECT * FROM AnalysisType_Audit)
$$

--
-- Audit Triggers For AnalysisType 
--


CREATE TRIGGER TrAI_AnalysisType_FER AFTER INSERT ON AnalysisType FOR EACH ROW
BEGIN
  INSERT INTO AnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisType
  , NEW.analysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_AnalysisType_FER AFTER UPDATE ON AnalysisType FOR EACH ROW
BEGIN
  INSERT INTO AnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysisType
  , NEW.analysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_AnalysisType_FER AFTER DELETE ON AnalysisType FOR EACH ROW
BEGIN
  INSERT INTO AnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysisType
  , OLD.analysisType
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For Analysis 
--

-- select 'Creating table Analysis'$$

-- DROP TABLE IF EXISTS `Analysis_Audit`$$

CREATE TABLE IF NOT EXISTS `Analysis_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisProtocol`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`privacyExpirationDate`  datetime  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Analysis 
--

INSERT INTO Analysis_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , number
  , name
  , description
  , idLab
  , idAnalysisType
  , idAnalysisProtocol
  , idOrganism
  , codeVisibility
  , createDate
  , idAppUser
  , idInstitution
  , idCoreFacility
  , privacyExpirationDate
  , idSubmitter )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnalysis
  , number
  , name
  , description
  , idLab
  , idAnalysisType
  , idAnalysisProtocol
  , idOrganism
  , codeVisibility
  , createDate
  , idAppUser
  , idInstitution
  , idCoreFacility
  , privacyExpirationDate
  , idSubmitter
  FROM Analysis
  WHERE NOT EXISTS(SELECT * FROM Analysis_Audit)
$$

--
-- Audit Triggers For Analysis 
--


CREATE TRIGGER TrAI_Analysis_FER AFTER INSERT ON Analysis FOR EACH ROW
BEGIN
  INSERT INTO Analysis_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , number
  , name
  , description
  , idLab
  , idAnalysisType
  , idAnalysisProtocol
  , idOrganism
  , codeVisibility
  , createDate
  , idAppUser
  , idInstitution
  , idCoreFacility
  , privacyExpirationDate
  , idSubmitter )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysis
  , NEW.number
  , NEW.name
  , NEW.description
  , NEW.idLab
  , NEW.idAnalysisType
  , NEW.idAnalysisProtocol
  , NEW.idOrganism
  , NEW.codeVisibility
  , NEW.createDate
  , NEW.idAppUser
  , NEW.idInstitution
  , NEW.idCoreFacility
  , NEW.privacyExpirationDate
  , NEW.idSubmitter );
END;
$$


CREATE TRIGGER TrAU_Analysis_FER AFTER UPDATE ON Analysis FOR EACH ROW
BEGIN
  INSERT INTO Analysis_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , number
  , name
  , description
  , idLab
  , idAnalysisType
  , idAnalysisProtocol
  , idOrganism
  , codeVisibility
  , createDate
  , idAppUser
  , idInstitution
  , idCoreFacility
  , privacyExpirationDate
  , idSubmitter )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnalysis
  , NEW.number
  , NEW.name
  , NEW.description
  , NEW.idLab
  , NEW.idAnalysisType
  , NEW.idAnalysisProtocol
  , NEW.idOrganism
  , NEW.codeVisibility
  , NEW.createDate
  , NEW.idAppUser
  , NEW.idInstitution
  , NEW.idCoreFacility
  , NEW.privacyExpirationDate
  , NEW.idSubmitter );
END;
$$


CREATE TRIGGER TrAD_Analysis_FER AFTER DELETE ON Analysis FOR EACH ROW
BEGIN
  INSERT INTO Analysis_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnalysis
  , number
  , name
  , description
  , idLab
  , idAnalysisType
  , idAnalysisProtocol
  , idOrganism
  , codeVisibility
  , createDate
  , idAppUser
  , idInstitution
  , idCoreFacility
  , privacyExpirationDate
  , idSubmitter )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnalysis
  , OLD.number
  , OLD.name
  , OLD.description
  , OLD.idLab
  , OLD.idAnalysisType
  , OLD.idAnalysisProtocol
  , OLD.idOrganism
  , OLD.codeVisibility
  , OLD.createDate
  , OLD.idAppUser
  , OLD.idInstitution
  , OLD.idCoreFacility
  , OLD.privacyExpirationDate
  , OLD.idSubmitter );
END;
$$


--
-- Audit Table For AnnotationReportField 
--

-- select 'Creating table AnnotationReportField'$$

-- DROP TABLE IF EXISTS `AnnotationReportField_Audit`$$

CREATE TABLE IF NOT EXISTS `AnnotationReportField_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAnnotationReportField`  int(10)  NULL DEFAULT NULL
 ,`source`  varchar(50)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`display`  varchar(50)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`dictionaryLookUpTable`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AnnotationReportField 
--

INSERT INTO AnnotationReportField_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable
  FROM AnnotationReportField
  WHERE NOT EXISTS(SELECT * FROM AnnotationReportField_Audit)
$$

--
-- Audit Triggers For AnnotationReportField 
--


CREATE TRIGGER TrAI_AnnotationReportField_FER AFTER INSERT ON AnnotationReportField FOR EACH ROW
BEGIN
  INSERT INTO AnnotationReportField_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAnnotationReportField
  , NEW.source
  , NEW.fieldName
  , NEW.display
  , NEW.isCustom
  , NEW.sortOrder
  , NEW.dictionaryLookUpTable );
END;
$$


CREATE TRIGGER TrAU_AnnotationReportField_FER AFTER UPDATE ON AnnotationReportField FOR EACH ROW
BEGIN
  INSERT INTO AnnotationReportField_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAnnotationReportField
  , NEW.source
  , NEW.fieldName
  , NEW.display
  , NEW.isCustom
  , NEW.sortOrder
  , NEW.dictionaryLookUpTable );
END;
$$


CREATE TRIGGER TrAD_AnnotationReportField_FER AFTER DELETE ON AnnotationReportField FOR EACH ROW
BEGIN
  INSERT INTO AnnotationReportField_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAnnotationReportField
  , OLD.source
  , OLD.fieldName
  , OLD.display
  , OLD.isCustom
  , OLD.sortOrder
  , OLD.dictionaryLookUpTable );
END;
$$


--
-- Audit Table For ApplicationTheme 
--

-- select 'Creating table ApplicationTheme'$$

-- DROP TABLE IF EXISTS `ApplicationTheme_Audit`$$

CREATE TABLE IF NOT EXISTS `ApplicationTheme_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idApplicationTheme`  int(10)  NULL DEFAULT NULL
 ,`applicationTheme`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ApplicationTheme 
--

INSERT INTO ApplicationTheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder
  FROM ApplicationTheme
  WHERE NOT EXISTS(SELECT * FROM ApplicationTheme_Audit)
$$

--
-- Audit Triggers For ApplicationTheme 
--


CREATE TRIGGER TrAI_ApplicationTheme_FER AFTER INSERT ON ApplicationTheme FOR EACH ROW
BEGIN
  INSERT INTO ApplicationTheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idApplicationTheme
  , NEW.applicationTheme
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_ApplicationTheme_FER AFTER UPDATE ON ApplicationTheme FOR EACH ROW
BEGIN
  INSERT INTO ApplicationTheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idApplicationTheme
  , NEW.applicationTheme
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_ApplicationTheme_FER AFTER DELETE ON ApplicationTheme FOR EACH ROW
BEGIN
  INSERT INTO ApplicationTheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idApplicationTheme
  , OLD.applicationTheme
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For ApplicationType 
--

-- select 'Creating table ApplicationType'$$

-- DROP TABLE IF EXISTS `ApplicationType_Audit`$$

CREATE TABLE IF NOT EXISTS `ApplicationType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeApplicationType`  varchar(10)  NULL DEFAULT NULL
 ,`applicationType`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ApplicationType 
--

INSERT INTO ApplicationType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplicationType
  , applicationType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeApplicationType
  , applicationType
  FROM ApplicationType
  WHERE NOT EXISTS(SELECT * FROM ApplicationType_Audit)
$$

--
-- Audit Triggers For ApplicationType 
--


CREATE TRIGGER TrAI_ApplicationType_FER AFTER INSERT ON ApplicationType FOR EACH ROW
BEGIN
  INSERT INTO ApplicationType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplicationType
  , applicationType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeApplicationType
  , NEW.applicationType );
END;
$$


CREATE TRIGGER TrAU_ApplicationType_FER AFTER UPDATE ON ApplicationType FOR EACH ROW
BEGIN
  INSERT INTO ApplicationType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplicationType
  , applicationType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeApplicationType
  , NEW.applicationType );
END;
$$


CREATE TRIGGER TrAD_ApplicationType_FER AFTER DELETE ON ApplicationType FOR EACH ROW
BEGIN
  INSERT INTO ApplicationType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplicationType
  , applicationType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeApplicationType
  , OLD.applicationType );
END;
$$


--
-- Audit Table For Application 
--

-- select 'Creating table Application'$$

-- DROP TABLE IF EXISTS `Application_Audit`$$

CREATE TABLE IF NOT EXISTS `Application_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`application`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idApplicationTheme`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeTo`  int(10)  NULL DEFAULT NULL
 ,`hasCaptureLibDesign`  char(1)  NULL DEFAULT NULL
 ,`coreSteps`  varchar(5000)  NULL DEFAULT NULL
 ,`coreStepsNoLibPrep`  varchar(5000)  NULL DEFAULT NULL
 ,`codeApplicationType`  varchar(10)  NULL DEFAULT NULL
 ,`onlyForLabPrepped`  char(1)  NULL DEFAULT NULL
 ,`samplesPerBatch`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`hasChipTypes`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Application 
--

INSERT INTO Application_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplication
  , application
  , isActive
  , idApplicationTheme
  , sortOrder
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , hasCaptureLibDesign
  , coreSteps
  , coreStepsNoLibPrep
  , codeApplicationType
  , onlyForLabPrepped
  , samplesPerBatch
  , idCoreFacility
  , hasChipTypes )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeApplication
  , application
  , isActive
  , idApplicationTheme
  , sortOrder
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , hasCaptureLibDesign
  , coreSteps
  , coreStepsNoLibPrep
  , codeApplicationType
  , onlyForLabPrepped
  , samplesPerBatch
  , idCoreFacility
  , hasChipTypes
  FROM Application
  WHERE NOT EXISTS(SELECT * FROM Application_Audit)
$$

--
-- Audit Triggers For Application 
--


CREATE TRIGGER TrAI_Application_FER AFTER INSERT ON Application FOR EACH ROW
BEGIN
  INSERT INTO Application_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplication
  , application
  , isActive
  , idApplicationTheme
  , sortOrder
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , hasCaptureLibDesign
  , coreSteps
  , coreStepsNoLibPrep
  , codeApplicationType
  , onlyForLabPrepped
  , samplesPerBatch
  , idCoreFacility
  , hasChipTypes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeApplication
  , NEW.application
  , NEW.isActive
  , NEW.idApplicationTheme
  , NEW.sortOrder
  , NEW.avgInsertSizeFrom
  , NEW.avgInsertSizeTo
  , NEW.hasCaptureLibDesign
  , NEW.coreSteps
  , NEW.coreStepsNoLibPrep
  , NEW.codeApplicationType
  , NEW.onlyForLabPrepped
  , NEW.samplesPerBatch
  , NEW.idCoreFacility
  , NEW.hasChipTypes );
END;
$$


CREATE TRIGGER TrAU_Application_FER AFTER UPDATE ON Application FOR EACH ROW
BEGIN
  INSERT INTO Application_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplication
  , application
  , isActive
  , idApplicationTheme
  , sortOrder
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , hasCaptureLibDesign
  , coreSteps
  , coreStepsNoLibPrep
  , codeApplicationType
  , onlyForLabPrepped
  , samplesPerBatch
  , idCoreFacility
  , hasChipTypes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeApplication
  , NEW.application
  , NEW.isActive
  , NEW.idApplicationTheme
  , NEW.sortOrder
  , NEW.avgInsertSizeFrom
  , NEW.avgInsertSizeTo
  , NEW.hasCaptureLibDesign
  , NEW.coreSteps
  , NEW.coreStepsNoLibPrep
  , NEW.codeApplicationType
  , NEW.onlyForLabPrepped
  , NEW.samplesPerBatch
  , NEW.idCoreFacility
  , NEW.hasChipTypes );
END;
$$


CREATE TRIGGER TrAD_Application_FER AFTER DELETE ON Application FOR EACH ROW
BEGIN
  INSERT INTO Application_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeApplication
  , application
  , isActive
  , idApplicationTheme
  , sortOrder
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , hasCaptureLibDesign
  , coreSteps
  , coreStepsNoLibPrep
  , codeApplicationType
  , onlyForLabPrepped
  , samplesPerBatch
  , idCoreFacility
  , hasChipTypes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeApplication
  , OLD.application
  , OLD.isActive
  , OLD.idApplicationTheme
  , OLD.sortOrder
  , OLD.avgInsertSizeFrom
  , OLD.avgInsertSizeTo
  , OLD.hasCaptureLibDesign
  , OLD.coreSteps
  , OLD.coreStepsNoLibPrep
  , OLD.codeApplicationType
  , OLD.onlyForLabPrepped
  , OLD.samplesPerBatch
  , OLD.idCoreFacility
  , OLD.hasChipTypes );
END;
$$


--
-- Audit Table For AppUser 
--

-- select 'Creating table AppUser'$$

-- DROP TABLE IF EXISTS `AppUser_Audit`$$

CREATE TABLE IF NOT EXISTS `AppUser_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`lastName`  varchar(200)  NULL DEFAULT NULL
 ,`firstName`  varchar(200)  NULL DEFAULT NULL
 ,`uNID`  varchar(50)  NULL DEFAULT NULL
 ,`email`  varchar(200)  NULL DEFAULT NULL
 ,`phone`  varchar(50)  NULL DEFAULT NULL
 ,`department`  varchar(100)  NULL DEFAULT NULL
 ,`institute`  varchar(100)  NULL DEFAULT NULL
 ,`jobTitle`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeUserPermissionKind`  varchar(10)  NULL DEFAULT NULL
 ,`userNameExternal`  varchar(100)  NULL DEFAULT NULL
 ,`passwordExternal`  varchar(100)  NULL DEFAULT NULL
 ,`ucscUrl`  varchar(250)  NULL DEFAULT NULL
 ,`salt`  varchar(300)  NULL DEFAULT NULL
 ,`guid`  varchar(100)  NULL DEFAULT NULL
 ,`guidExpiration`  datetime  NULL DEFAULT NULL
 ,`passwordExpired`  char(1)  NULL DEFAULT NULL
 ,`confirmEmailGuid`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for AppUser 
--

INSERT INTO AppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAppUser
  , lastName
  , firstName
  , uNID
  , email
  , phone
  , department
  , institute
  , jobTitle
  , isActive
  , codeUserPermissionKind
  , userNameExternal
  , passwordExternal
  , ucscUrl
  , salt
  , guid
  , guidExpiration
  , passwordExpired
  , confirmEmailGuid )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAppUser
  , lastName
  , firstName
  , uNID
  , email
  , phone
  , department
  , institute
  , jobTitle
  , isActive
  , codeUserPermissionKind
  , userNameExternal
  , passwordExternal
  , ucscUrl
  , salt
  , guid
  , guidExpiration
  , passwordExpired
  , confirmEmailGuid
  FROM AppUser
  WHERE NOT EXISTS(SELECT * FROM AppUser_Audit)
$$

--
-- Audit Triggers For AppUser 
--


CREATE TRIGGER TrAI_AppUser_FER AFTER INSERT ON AppUser FOR EACH ROW
BEGIN
  INSERT INTO AppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAppUser
  , lastName
  , firstName
  , uNID
  , email
  , phone
  , department
  , institute
  , jobTitle
  , isActive
  , codeUserPermissionKind
  , userNameExternal
  , passwordExternal
  , ucscUrl
  , salt
  , guid
  , guidExpiration
  , passwordExpired
  , confirmEmailGuid )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAppUser
  , NEW.lastName
  , NEW.firstName
  , NEW.uNID
  , NEW.email
  , NEW.phone
  , NEW.department
  , NEW.institute
  , NEW.jobTitle
  , NEW.isActive
  , NEW.codeUserPermissionKind
  , NEW.userNameExternal
  , NEW.passwordExternal
  , NEW.ucscUrl
  , NEW.salt
  , NEW.guid
  , NEW.guidExpiration
  , NEW.passwordExpired
  , NEW.confirmEmailGuid );
END;
$$


CREATE TRIGGER TrAU_AppUser_FER AFTER UPDATE ON AppUser FOR EACH ROW
BEGIN
  INSERT INTO AppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAppUser
  , lastName
  , firstName
  , uNID
  , email
  , phone
  , department
  , institute
  , jobTitle
  , isActive
  , codeUserPermissionKind
  , userNameExternal
  , passwordExternal
  , ucscUrl
  , salt
  , guid
  , guidExpiration
  , passwordExpired
  , confirmEmailGuid )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAppUser
  , NEW.lastName
  , NEW.firstName
  , NEW.uNID
  , NEW.email
  , NEW.phone
  , NEW.department
  , NEW.institute
  , NEW.jobTitle
  , NEW.isActive
  , NEW.codeUserPermissionKind
  , NEW.userNameExternal
  , NEW.passwordExternal
  , NEW.ucscUrl
  , NEW.salt
  , NEW.guid
  , NEW.guidExpiration
  , NEW.passwordExpired
  , NEW.confirmEmailGuid );
END;
$$


CREATE TRIGGER TrAD_AppUser_FER AFTER DELETE ON AppUser FOR EACH ROW
BEGIN
  INSERT INTO AppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAppUser
  , lastName
  , firstName
  , uNID
  , email
  , phone
  , department
  , institute
  , jobTitle
  , isActive
  , codeUserPermissionKind
  , userNameExternal
  , passwordExternal
  , ucscUrl
  , salt
  , guid
  , guidExpiration
  , passwordExpired
  , confirmEmailGuid )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAppUser
  , OLD.lastName
  , OLD.firstName
  , OLD.uNID
  , OLD.email
  , OLD.phone
  , OLD.department
  , OLD.institute
  , OLD.jobTitle
  , OLD.isActive
  , OLD.codeUserPermissionKind
  , OLD.userNameExternal
  , OLD.passwordExternal
  , OLD.ucscUrl
  , OLD.salt
  , OLD.guid
  , OLD.guidExpiration
  , OLD.passwordExpired
  , OLD.confirmEmailGuid );
END;
$$


--
-- Audit Table For ArrayCoordinate 
--

-- select 'Creating table ArrayCoordinate'$$

-- DROP TABLE IF EXISTS `ArrayCoordinate_Audit`$$

CREATE TABLE IF NOT EXISTS `ArrayCoordinate_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`x`  int(10)  NULL DEFAULT NULL
 ,`y`  int(10)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ArrayCoordinate 
--

INSERT INTO ArrayCoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign
  FROM ArrayCoordinate
  WHERE NOT EXISTS(SELECT * FROM ArrayCoordinate_Audit)
$$

--
-- Audit Triggers For ArrayCoordinate 
--


CREATE TRIGGER TrAI_ArrayCoordinate_FER AFTER INSERT ON ArrayCoordinate FOR EACH ROW
BEGIN
  INSERT INTO ArrayCoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idArrayCoordinate
  , NEW.name
  , NEW.x
  , NEW.y
  , NEW.idSlideDesign );
END;
$$


CREATE TRIGGER TrAU_ArrayCoordinate_FER AFTER UPDATE ON ArrayCoordinate FOR EACH ROW
BEGIN
  INSERT INTO ArrayCoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idArrayCoordinate
  , NEW.name
  , NEW.x
  , NEW.y
  , NEW.idSlideDesign );
END;
$$


CREATE TRIGGER TrAD_ArrayCoordinate_FER AFTER DELETE ON ArrayCoordinate FOR EACH ROW
BEGIN
  INSERT INTO ArrayCoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idArrayCoordinate
  , OLD.name
  , OLD.x
  , OLD.y
  , OLD.idSlideDesign );
END;
$$


--
-- Audit Table For ArrayDesign 
--

-- select 'Creating table ArrayDesign'$$

-- DROP TABLE IF EXISTS `ArrayDesign_Audit`$$

CREATE TABLE IF NOT EXISTS `ArrayDesign_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idArrayDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`accessionNumberUArrayExpress`  varchar(100)  NULL DEFAULT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ArrayDesign 
--

INSERT INTO ArrayDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate
  FROM ArrayDesign
  WHERE NOT EXISTS(SELECT * FROM ArrayDesign_Audit)
$$

--
-- Audit Triggers For ArrayDesign 
--


CREATE TRIGGER TrAI_ArrayDesign_FER AFTER INSERT ON ArrayDesign FOR EACH ROW
BEGIN
  INSERT INTO ArrayDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idArrayDesign
  , NEW.name
  , NEW.accessionNumberUArrayExpress
  , NEW.idArrayCoordinate );
END;
$$


CREATE TRIGGER TrAU_ArrayDesign_FER AFTER UPDATE ON ArrayDesign FOR EACH ROW
BEGIN
  INSERT INTO ArrayDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idArrayDesign
  , NEW.name
  , NEW.accessionNumberUArrayExpress
  , NEW.idArrayCoordinate );
END;
$$


CREATE TRIGGER TrAD_ArrayDesign_FER AFTER DELETE ON ArrayDesign FOR EACH ROW
BEGIN
  INSERT INTO ArrayDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idArrayDesign
  , OLD.name
  , OLD.accessionNumberUArrayExpress
  , OLD.idArrayCoordinate );
END;
$$


--
-- Audit Table For Assay 
--

-- select 'Creating table Assay'$$

-- DROP TABLE IF EXISTS `Assay_Audit`$$

CREATE TABLE IF NOT EXISTS `Assay_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idAssay`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Assay 
--

INSERT INTO Assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAssay
  , name
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idAssay
  , name
  , description
  , isActive
  FROM Assay
  WHERE NOT EXISTS(SELECT * FROM Assay_Audit)
$$

--
-- Audit Triggers For Assay 
--


CREATE TRIGGER TrAI_Assay_FER AFTER INSERT ON Assay FOR EACH ROW
BEGIN
  INSERT INTO Assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idAssay
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Assay_FER AFTER UPDATE ON Assay FOR EACH ROW
BEGIN
  INSERT INTO Assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idAssay
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Assay_FER AFTER DELETE ON Assay FOR EACH ROW
BEGIN
  INSERT INTO Assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idAssay
  , OLD.name
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingAccountUser 
--

-- select 'Creating table BillingAccountUser'$$

-- DROP TABLE IF EXISTS `BillingAccountUser_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingAccountUser_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingAccountUser 
--

INSERT INTO BillingAccountUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingAccount
  , idAppUser
  FROM BillingAccountUser
  WHERE NOT EXISTS(SELECT * FROM BillingAccountUser_Audit)
$$

--
-- Audit Triggers For BillingAccountUser 
--


CREATE TRIGGER TrAI_BillingAccountUser_FER AFTER INSERT ON BillingAccountUser FOR EACH ROW
BEGIN
  INSERT INTO BillingAccountUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingAccount
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_BillingAccountUser_FER AFTER UPDATE ON BillingAccountUser FOR EACH ROW
BEGIN
  INSERT INTO BillingAccountUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingAccount
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_BillingAccountUser_FER AFTER DELETE ON BillingAccountUser FOR EACH ROW
BEGIN
  INSERT INTO BillingAccountUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingAccount
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For BillingAccount 
--

-- select 'Creating table BillingAccount'$$

-- DROP TABLE IF EXISTS `BillingAccount_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingAccount_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`accountName`  varchar(200)  NULL DEFAULT NULL
 ,`accountNumber`  varchar(100)  NULL DEFAULT NULL
 ,`expirationDate`  datetime  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`accountNumberBus`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberOrg`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberFund`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberActivity`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberProject`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberAccount`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberAu`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberYear`  varchar(10)  NULL DEFAULT NULL
 ,`idFundingAgency`  int(10)  NULL DEFAULT NULL
 ,`idCreditCardCompany`  int(10)  NULL DEFAULT NULL
 ,`isPO`  char(1)  NULL DEFAULT NULL
 ,`isCreditCard`  char(1)  NULL DEFAULT NULL
 ,`zipCode`  varchar(20)  NULL DEFAULT NULL
 ,`isApproved`  char(1)  NULL DEFAULT NULL
 ,`approvedDate`  datetime  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`submitterEmail`  varchar(200)  NULL DEFAULT NULL
 ,`submitterUID`  varchar(50)  NULL DEFAULT NULL
 ,`totalDollarAmount`  decimal(12,2)  NULL DEFAULT NULL
 ,`purchaseOrderForm`  longblob  NULL DEFAULT NULL
 ,`orderFormFileType`  varchar(10)  NULL DEFAULT NULL
 ,`orderFormFileSize`  bigint(20)  NULL DEFAULT NULL
 ,`shortAcct`  varchar(10)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`custom1`  varchar(50)  NULL DEFAULT NULL
 ,`custom2`  varchar(50)  NULL DEFAULT NULL
 ,`custom3`  varchar(50)  NULL DEFAULT NULL
 ,`approverEmail`  varchar(200)  NULL DEFAULT NULL
 ,`idApprover`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingAccount 
--

INSERT INTO BillingAccount_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , accountName
  , accountNumber
  , expirationDate
  , idLab
  , accountNumberBus
  , accountNumberOrg
  , accountNumberFund
  , accountNumberActivity
  , accountNumberProject
  , accountNumberAccount
  , accountNumberAu
  , accountNumberYear
  , idFundingAgency
  , idCreditCardCompany
  , isPO
  , isCreditCard
  , zipCode
  , isApproved
  , approvedDate
  , createDate
  , submitterEmail
  , submitterUID
  , totalDollarAmount
  , purchaseOrderForm
  , orderFormFileType
  , orderFormFileSize
  , shortAcct
  , startDate
  , idCoreFacility
  , custom1
  , custom2
  , custom3
  , approverEmail
  , idApprover )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingAccount
  , accountName
  , accountNumber
  , expirationDate
  , idLab
  , accountNumberBus
  , accountNumberOrg
  , accountNumberFund
  , accountNumberActivity
  , accountNumberProject
  , accountNumberAccount
  , accountNumberAu
  , accountNumberYear
  , idFundingAgency
  , idCreditCardCompany
  , isPO
  , isCreditCard
  , zipCode
  , isApproved
  , approvedDate
  , createDate
  , submitterEmail
  , submitterUID
  , totalDollarAmount
  , purchaseOrderForm
  , orderFormFileType
  , orderFormFileSize
  , shortAcct
  , startDate
  , idCoreFacility
  , custom1
  , custom2
  , custom3
  , approverEmail
  , idApprover
  FROM BillingAccount
  WHERE NOT EXISTS(SELECT * FROM BillingAccount_Audit)
$$

--
-- Audit Triggers For BillingAccount 
--


CREATE TRIGGER TrAI_BillingAccount_FER AFTER INSERT ON BillingAccount FOR EACH ROW
BEGIN
  INSERT INTO BillingAccount_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , accountName
  , accountNumber
  , expirationDate
  , idLab
  , accountNumberBus
  , accountNumberOrg
  , accountNumberFund
  , accountNumberActivity
  , accountNumberProject
  , accountNumberAccount
  , accountNumberAu
  , accountNumberYear
  , idFundingAgency
  , idCreditCardCompany
  , isPO
  , isCreditCard
  , zipCode
  , isApproved
  , approvedDate
  , createDate
  , submitterEmail
  , submitterUID
  , totalDollarAmount
  , purchaseOrderForm
  , orderFormFileType
  , orderFormFileSize
  , shortAcct
  , startDate
  , idCoreFacility
  , custom1
  , custom2
  , custom3
  , approverEmail
  , idApprover )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingAccount
  , NEW.accountName
  , NEW.accountNumber
  , NEW.expirationDate
  , NEW.idLab
  , NEW.accountNumberBus
  , NEW.accountNumberOrg
  , NEW.accountNumberFund
  , NEW.accountNumberActivity
  , NEW.accountNumberProject
  , NEW.accountNumberAccount
  , NEW.accountNumberAu
  , NEW.accountNumberYear
  , NEW.idFundingAgency
  , NEW.idCreditCardCompany
  , NEW.isPO
  , NEW.isCreditCard
  , NEW.zipCode
  , NEW.isApproved
  , NEW.approvedDate
  , NEW.createDate
  , NEW.submitterEmail
  , NEW.submitterUID
  , NEW.totalDollarAmount
  , NEW.purchaseOrderForm
  , NEW.orderFormFileType
  , NEW.orderFormFileSize
  , NEW.shortAcct
  , NEW.startDate
  , NEW.idCoreFacility
  , NEW.custom1
  , NEW.custom2
  , NEW.custom3
  , NEW.approverEmail
  , NEW.idApprover );
END;
$$


CREATE TRIGGER TrAU_BillingAccount_FER AFTER UPDATE ON BillingAccount FOR EACH ROW
BEGIN
  INSERT INTO BillingAccount_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , accountName
  , accountNumber
  , expirationDate
  , idLab
  , accountNumberBus
  , accountNumberOrg
  , accountNumberFund
  , accountNumberActivity
  , accountNumberProject
  , accountNumberAccount
  , accountNumberAu
  , accountNumberYear
  , idFundingAgency
  , idCreditCardCompany
  , isPO
  , isCreditCard
  , zipCode
  , isApproved
  , approvedDate
  , createDate
  , submitterEmail
  , submitterUID
  , totalDollarAmount
  , purchaseOrderForm
  , orderFormFileType
  , orderFormFileSize
  , shortAcct
  , startDate
  , idCoreFacility
  , custom1
  , custom2
  , custom3
  , approverEmail
  , idApprover )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingAccount
  , NEW.accountName
  , NEW.accountNumber
  , NEW.expirationDate
  , NEW.idLab
  , NEW.accountNumberBus
  , NEW.accountNumberOrg
  , NEW.accountNumberFund
  , NEW.accountNumberActivity
  , NEW.accountNumberProject
  , NEW.accountNumberAccount
  , NEW.accountNumberAu
  , NEW.accountNumberYear
  , NEW.idFundingAgency
  , NEW.idCreditCardCompany
  , NEW.isPO
  , NEW.isCreditCard
  , NEW.zipCode
  , NEW.isApproved
  , NEW.approvedDate
  , NEW.createDate
  , NEW.submitterEmail
  , NEW.submitterUID
  , NEW.totalDollarAmount
  , NEW.purchaseOrderForm
  , NEW.orderFormFileType
  , NEW.orderFormFileSize
  , NEW.shortAcct
  , NEW.startDate
  , NEW.idCoreFacility
  , NEW.custom1
  , NEW.custom2
  , NEW.custom3
  , NEW.approverEmail
  , NEW.idApprover );
END;
$$


CREATE TRIGGER TrAD_BillingAccount_FER AFTER DELETE ON BillingAccount FOR EACH ROW
BEGIN
  INSERT INTO BillingAccount_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingAccount
  , accountName
  , accountNumber
  , expirationDate
  , idLab
  , accountNumberBus
  , accountNumberOrg
  , accountNumberFund
  , accountNumberActivity
  , accountNumberProject
  , accountNumberAccount
  , accountNumberAu
  , accountNumberYear
  , idFundingAgency
  , idCreditCardCompany
  , isPO
  , isCreditCard
  , zipCode
  , isApproved
  , approvedDate
  , createDate
  , submitterEmail
  , submitterUID
  , totalDollarAmount
  , purchaseOrderForm
  , orderFormFileType
  , orderFormFileSize
  , shortAcct
  , startDate
  , idCoreFacility
  , custom1
  , custom2
  , custom3
  , approverEmail
  , idApprover )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingAccount
  , OLD.accountName
  , OLD.accountNumber
  , OLD.expirationDate
  , OLD.idLab
  , OLD.accountNumberBus
  , OLD.accountNumberOrg
  , OLD.accountNumberFund
  , OLD.accountNumberActivity
  , OLD.accountNumberProject
  , OLD.accountNumberAccount
  , OLD.accountNumberAu
  , OLD.accountNumberYear
  , OLD.idFundingAgency
  , OLD.idCreditCardCompany
  , OLD.isPO
  , OLD.isCreditCard
  , OLD.zipCode
  , OLD.isApproved
  , OLD.approvedDate
  , OLD.createDate
  , OLD.submitterEmail
  , OLD.submitterUID
  , OLD.totalDollarAmount
  , OLD.purchaseOrderForm
  , OLD.orderFormFileType
  , OLD.orderFormFileSize
  , OLD.shortAcct
  , OLD.startDate
  , OLD.idCoreFacility
  , OLD.custom1
  , OLD.custom2
  , OLD.custom3
  , OLD.approverEmail
  , OLD.idApprover );
END;
$$


--
-- Audit Table For BillingChargeKind 
--

-- select 'Creating table BillingChargeKind'$$

-- DROP TABLE IF EXISTS `BillingChargeKind_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingChargeKind_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`billingChargeKind`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingChargeKind 
--

INSERT INTO BillingChargeKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeBillingChargeKind
  , billingChargeKind
  , isActive
  FROM BillingChargeKind
  WHERE NOT EXISTS(SELECT * FROM BillingChargeKind_Audit)
$$

--
-- Audit Triggers For BillingChargeKind 
--


CREATE TRIGGER TrAI_BillingChargeKind_FER AFTER INSERT ON BillingChargeKind FOR EACH ROW
BEGIN
  INSERT INTO BillingChargeKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeBillingChargeKind
  , NEW.billingChargeKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_BillingChargeKind_FER AFTER UPDATE ON BillingChargeKind FOR EACH ROW
BEGIN
  INSERT INTO BillingChargeKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeBillingChargeKind
  , NEW.billingChargeKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_BillingChargeKind_FER AFTER DELETE ON BillingChargeKind FOR EACH ROW
BEGIN
  INSERT INTO BillingChargeKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeBillingChargeKind
  , OLD.billingChargeKind
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingItem 
--

-- select 'Creating table BillingItem'$$

-- DROP TABLE IF EXISTS `BillingItem_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingItem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingItem`  int(10)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`category`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`invoicePrice`  decimal(9,2)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`codeBillingStatus`  varchar(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`percentagePrice`  decimal(4,3)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`completeDate`  datetime  NULL DEFAULT NULL
 ,`splitType`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idDiskUsageByMonth`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idMasterBillingItem`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingItem 
--

INSERT INTO BillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingItem
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , invoicePrice
  , idBillingPeriod
  , codeBillingStatus
  , idPriceCategory
  , idPrice
  , idRequest
  , idBillingAccount
  , percentagePrice
  , notes
  , idLab
  , completeDate
  , splitType
  , idCoreFacility
  , idInvoice
  , idDiskUsageByMonth
  , idProductOrder
  , idMasterBillingItem )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingItem
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , invoicePrice
  , idBillingPeriod
  , codeBillingStatus
  , idPriceCategory
  , idPrice
  , idRequest
  , idBillingAccount
  , percentagePrice
  , notes
  , idLab
  , completeDate
  , splitType
  , idCoreFacility
  , idInvoice
  , idDiskUsageByMonth
  , idProductOrder
  , idMasterBillingItem
  FROM BillingItem
  WHERE NOT EXISTS(SELECT * FROM BillingItem_Audit)
$$

--
-- Audit Triggers For BillingItem 
--


CREATE TRIGGER TrAI_BillingItem_FER AFTER INSERT ON BillingItem FOR EACH ROW
BEGIN
  INSERT INTO BillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingItem
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , invoicePrice
  , idBillingPeriod
  , codeBillingStatus
  , idPriceCategory
  , idPrice
  , idRequest
  , idBillingAccount
  , percentagePrice
  , notes
  , idLab
  , completeDate
  , splitType
  , idCoreFacility
  , idInvoice
  , idDiskUsageByMonth
  , idProductOrder
  , idMasterBillingItem )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingItem
  , NEW.codeBillingChargeKind
  , NEW.category
  , NEW.description
  , NEW.qty
  , NEW.unitPrice
  , NEW.invoicePrice
  , NEW.idBillingPeriod
  , NEW.codeBillingStatus
  , NEW.idPriceCategory
  , NEW.idPrice
  , NEW.idRequest
  , NEW.idBillingAccount
  , NEW.percentagePrice
  , NEW.notes
  , NEW.idLab
  , NEW.completeDate
  , NEW.splitType
  , NEW.idCoreFacility
  , NEW.idInvoice
  , NEW.idDiskUsageByMonth
  , NEW.idProductOrder
  , NEW.idMasterBillingItem );
END;
$$


CREATE TRIGGER TrAU_BillingItem_FER AFTER UPDATE ON BillingItem FOR EACH ROW
BEGIN
  INSERT INTO BillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingItem
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , invoicePrice
  , idBillingPeriod
  , codeBillingStatus
  , idPriceCategory
  , idPrice
  , idRequest
  , idBillingAccount
  , percentagePrice
  , notes
  , idLab
  , completeDate
  , splitType
  , idCoreFacility
  , idInvoice
  , idDiskUsageByMonth
  , idProductOrder
  , idMasterBillingItem )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingItem
  , NEW.codeBillingChargeKind
  , NEW.category
  , NEW.description
  , NEW.qty
  , NEW.unitPrice
  , NEW.invoicePrice
  , NEW.idBillingPeriod
  , NEW.codeBillingStatus
  , NEW.idPriceCategory
  , NEW.idPrice
  , NEW.idRequest
  , NEW.idBillingAccount
  , NEW.percentagePrice
  , NEW.notes
  , NEW.idLab
  , NEW.completeDate
  , NEW.splitType
  , NEW.idCoreFacility
  , NEW.idInvoice
  , NEW.idDiskUsageByMonth
  , NEW.idProductOrder
  , NEW.idMasterBillingItem );
END;
$$


CREATE TRIGGER TrAD_BillingItem_FER AFTER DELETE ON BillingItem FOR EACH ROW
BEGIN
  INSERT INTO BillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingItem
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , invoicePrice
  , idBillingPeriod
  , codeBillingStatus
  , idPriceCategory
  , idPrice
  , idRequest
  , idBillingAccount
  , percentagePrice
  , notes
  , idLab
  , completeDate
  , splitType
  , idCoreFacility
  , idInvoice
  , idDiskUsageByMonth
  , idProductOrder
  , idMasterBillingItem )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingItem
  , OLD.codeBillingChargeKind
  , OLD.category
  , OLD.description
  , OLD.qty
  , OLD.unitPrice
  , OLD.invoicePrice
  , OLD.idBillingPeriod
  , OLD.codeBillingStatus
  , OLD.idPriceCategory
  , OLD.idPrice
  , OLD.idRequest
  , OLD.idBillingAccount
  , OLD.percentagePrice
  , OLD.notes
  , OLD.idLab
  , OLD.completeDate
  , OLD.splitType
  , OLD.idCoreFacility
  , OLD.idInvoice
  , OLD.idDiskUsageByMonth
  , OLD.idProductOrder
  , OLD.idMasterBillingItem );
END;
$$


--
-- Audit Table For Billingperiod 
--

-- select 'Creating table Billingperiod'$$

-- DROP TABLE IF EXISTS `Billingperiod_Audit`$$

CREATE TABLE IF NOT EXISTS `Billingperiod_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`billingPeriod`  varchar(50)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`endDate`  datetime  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Billingperiod 
--

INSERT INTO Billingperiod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive
  FROM Billingperiod
  WHERE NOT EXISTS(SELECT * FROM Billingperiod_Audit)
$$

--
-- Audit Triggers For Billingperiod 
--


CREATE TRIGGER TrAI_Billingperiod_FER AFTER INSERT ON Billingperiod FOR EACH ROW
BEGIN
  INSERT INTO Billingperiod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingPeriod
  , NEW.billingPeriod
  , NEW.startDate
  , NEW.endDate
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Billingperiod_FER AFTER UPDATE ON Billingperiod FOR EACH ROW
BEGIN
  INSERT INTO Billingperiod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingPeriod
  , NEW.billingPeriod
  , NEW.startDate
  , NEW.endDate
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Billingperiod_FER AFTER DELETE ON Billingperiod FOR EACH ROW
BEGIN
  INSERT INTO Billingperiod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingPeriod
  , OLD.billingPeriod
  , OLD.startDate
  , OLD.endDate
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingSlideProductClass 
--

-- select 'Creating table BillingSlideProductClass'$$

-- DROP TABLE IF EXISTS `BillingSlideProductClass_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingSlideProductClass_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingSlideProductClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideProductClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingSlideProductClass 
--

INSERT INTO BillingSlideProductClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive
  FROM BillingSlideProductClass
  WHERE NOT EXISTS(SELECT * FROM BillingSlideProductClass_Audit)
$$

--
-- Audit Triggers For BillingSlideProductClass 
--


CREATE TRIGGER TrAI_BillingSlideProductClass_FER AFTER INSERT ON BillingSlideProductClass FOR EACH ROW
BEGIN
  INSERT INTO BillingSlideProductClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingSlideProductClass
  , NEW.billingSlideProductClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_BillingSlideProductClass_FER AFTER UPDATE ON BillingSlideProductClass FOR EACH ROW
BEGIN
  INSERT INTO BillingSlideProductClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingSlideProductClass
  , NEW.billingSlideProductClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_BillingSlideProductClass_FER AFTER DELETE ON BillingSlideProductClass FOR EACH ROW
BEGIN
  INSERT INTO BillingSlideProductClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingSlideProductClass
  , OLD.billingSlideProductClass
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingSlideServiceClass 
--

-- select 'Creating table BillingSlideServiceClass'$$

-- DROP TABLE IF EXISTS `BillingSlideServiceClass_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingSlideServiceClass_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideServiceClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingSlideServiceClass 
--

INSERT INTO BillingSlideServiceClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive
  FROM BillingSlideServiceClass
  WHERE NOT EXISTS(SELECT * FROM BillingSlideServiceClass_Audit)
$$

--
-- Audit Triggers For BillingSlideServiceClass 
--


CREATE TRIGGER TrAI_BillingSlideServiceClass_FER AFTER INSERT ON BillingSlideServiceClass FOR EACH ROW
BEGIN
  INSERT INTO BillingSlideServiceClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingSlideServiceClass
  , NEW.billingSlideServiceClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_BillingSlideServiceClass_FER AFTER UPDATE ON BillingSlideServiceClass FOR EACH ROW
BEGIN
  INSERT INTO BillingSlideServiceClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingSlideServiceClass
  , NEW.billingSlideServiceClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_BillingSlideServiceClass_FER AFTER DELETE ON BillingSlideServiceClass FOR EACH ROW
BEGIN
  INSERT INTO BillingSlideServiceClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingSlideServiceClass
  , OLD.billingSlideServiceClass
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingStatus 
--

-- select 'Creating table BillingStatus'$$

-- DROP TABLE IF EXISTS `BillingStatus_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingStatus_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeBillingStatus`  varchar(10)  NULL DEFAULT NULL
 ,`billingStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingStatus 
--

INSERT INTO BillingStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingStatus
  , billingStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeBillingStatus
  , billingStatus
  , isActive
  FROM BillingStatus
  WHERE NOT EXISTS(SELECT * FROM BillingStatus_Audit)
$$

--
-- Audit Triggers For BillingStatus 
--


CREATE TRIGGER TrAI_BillingStatus_FER AFTER INSERT ON BillingStatus FOR EACH ROW
BEGIN
  INSERT INTO BillingStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeBillingStatus
  , NEW.billingStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_BillingStatus_FER AFTER UPDATE ON BillingStatus FOR EACH ROW
BEGIN
  INSERT INTO BillingStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeBillingStatus
  , NEW.billingStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_BillingStatus_FER AFTER DELETE ON BillingStatus FOR EACH ROW
BEGIN
  INSERT INTO BillingStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeBillingStatus
  , OLD.billingStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingTemplateItem 
--

-- select 'Creating table BillingTemplateItem'$$

-- DROP TABLE IF EXISTS `BillingTemplateItem_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingTemplateItem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingTemplateItem`  int(10)  NULL DEFAULT NULL
 ,`idBillingTemplate`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`percentSplit`  decimal(4,3)  NULL DEFAULT NULL
 ,`dollarAmount`  decimal(7,2)  NULL DEFAULT NULL
 ,`dollarAmountBalance`  decimal(7,2)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingTemplateItem 
--

INSERT INTO BillingTemplateItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplateItem
  , idBillingTemplate
  , idBillingAccount
  , percentSplit
  , dollarAmount
  , dollarAmountBalance
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingTemplateItem
  , idBillingTemplate
  , idBillingAccount
  , percentSplit
  , dollarAmount
  , dollarAmountBalance
  , sortOrder
  FROM BillingTemplateItem
  WHERE NOT EXISTS(SELECT * FROM BillingTemplateItem_Audit)
$$

--
-- Audit Triggers For BillingTemplateItem 
--


CREATE TRIGGER TrAI_BillingTemplateItem_FER AFTER INSERT ON BillingTemplateItem FOR EACH ROW
BEGIN
  INSERT INTO BillingTemplateItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplateItem
  , idBillingTemplate
  , idBillingAccount
  , percentSplit
  , dollarAmount
  , dollarAmountBalance
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingTemplateItem
  , NEW.idBillingTemplate
  , NEW.idBillingAccount
  , NEW.percentSplit
  , NEW.dollarAmount
  , NEW.dollarAmountBalance
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_BillingTemplateItem_FER AFTER UPDATE ON BillingTemplateItem FOR EACH ROW
BEGIN
  INSERT INTO BillingTemplateItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplateItem
  , idBillingTemplate
  , idBillingAccount
  , percentSplit
  , dollarAmount
  , dollarAmountBalance
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingTemplateItem
  , NEW.idBillingTemplate
  , NEW.idBillingAccount
  , NEW.percentSplit
  , NEW.dollarAmount
  , NEW.dollarAmountBalance
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_BillingTemplateItem_FER AFTER DELETE ON BillingTemplateItem FOR EACH ROW
BEGIN
  INSERT INTO BillingTemplateItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplateItem
  , idBillingTemplate
  , idBillingAccount
  , percentSplit
  , dollarAmount
  , dollarAmountBalance
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingTemplateItem
  , OLD.idBillingTemplate
  , OLD.idBillingAccount
  , OLD.percentSplit
  , OLD.dollarAmount
  , OLD.dollarAmountBalance
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For BillingTemplate 
--

-- select 'Creating table BillingTemplate'$$

-- DROP TABLE IF EXISTS `BillingTemplate_Audit`$$

CREATE TABLE IF NOT EXISTS `BillingTemplate_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idBillingTemplate`  int(10)  NULL DEFAULT NULL
 ,`targetClassIdentifier`  int(10)  NULL DEFAULT NULL
 ,`targetClassName`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BillingTemplate 
--

INSERT INTO BillingTemplate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplate
  , targetClassIdentifier
  , targetClassName )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idBillingTemplate
  , targetClassIdentifier
  , targetClassName
  FROM BillingTemplate
  WHERE NOT EXISTS(SELECT * FROM BillingTemplate_Audit)
$$

--
-- Audit Triggers For BillingTemplate 
--


CREATE TRIGGER TrAI_BillingTemplate_FER AFTER INSERT ON BillingTemplate FOR EACH ROW
BEGIN
  INSERT INTO BillingTemplate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplate
  , targetClassIdentifier
  , targetClassName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingTemplate
  , NEW.targetClassIdentifier
  , NEW.targetClassName );
END;
$$


CREATE TRIGGER TrAU_BillingTemplate_FER AFTER UPDATE ON BillingTemplate FOR EACH ROW
BEGIN
  INSERT INTO BillingTemplate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplate
  , targetClassIdentifier
  , targetClassName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idBillingTemplate
  , NEW.targetClassIdentifier
  , NEW.targetClassName );
END;
$$


CREATE TRIGGER TrAD_BillingTemplate_FER AFTER DELETE ON BillingTemplate FOR EACH ROW
BEGIN
  INSERT INTO BillingTemplate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idBillingTemplate
  , targetClassIdentifier
  , targetClassName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idBillingTemplate
  , OLD.targetClassIdentifier
  , OLD.targetClassName );
END;
$$


--
-- Audit Table For BioanalyzerChipType 
--

-- select 'Creating table BioanalyzerChipType'$$

-- DROP TABLE IF EXISTS `BioanalyzerChipType_Audit`$$

CREATE TABLE IF NOT EXISTS `BioanalyzerChipType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`bioanalyzerChipType`  varchar(100)  NULL DEFAULT NULL
 ,`concentrationRange`  varchar(50)  NULL DEFAULT NULL
 ,`maxSampleBufferStrength`  varchar(50)  NULL DEFAULT NULL
 ,`costPerSample`  decimal(5,2)  NULL DEFAULT NULL
 ,`sampleWellsPerChip`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`protocolDescription`  longtext  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for BioanalyzerChipType 
--

INSERT INTO BioanalyzerChipType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription
  , sortOrder
  FROM BioanalyzerChipType
  WHERE NOT EXISTS(SELECT * FROM BioanalyzerChipType_Audit)
$$

--
-- Audit Triggers For BioanalyzerChipType 
--


CREATE TRIGGER TrAI_BioanalyzerChipType_FER AFTER INSERT ON BioanalyzerChipType FOR EACH ROW
BEGIN
  INSERT INTO BioanalyzerChipType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeBioanalyzerChipType
  , NEW.bioanalyzerChipType
  , NEW.concentrationRange
  , NEW.maxSampleBufferStrength
  , NEW.costPerSample
  , NEW.sampleWellsPerChip
  , NEW.isActive
  , NEW.codeConcentrationUnit
  , NEW.codeApplication
  , NEW.protocolDescription
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_BioanalyzerChipType_FER AFTER UPDATE ON BioanalyzerChipType FOR EACH ROW
BEGIN
  INSERT INTO BioanalyzerChipType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeBioanalyzerChipType
  , NEW.bioanalyzerChipType
  , NEW.concentrationRange
  , NEW.maxSampleBufferStrength
  , NEW.costPerSample
  , NEW.sampleWellsPerChip
  , NEW.isActive
  , NEW.codeConcentrationUnit
  , NEW.codeApplication
  , NEW.protocolDescription
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_BioanalyzerChipType_FER AFTER DELETE ON BioanalyzerChipType FOR EACH ROW
BEGIN
  INSERT INTO BioanalyzerChipType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeBioanalyzerChipType
  , OLD.bioanalyzerChipType
  , OLD.concentrationRange
  , OLD.maxSampleBufferStrength
  , OLD.costPerSample
  , OLD.sampleWellsPerChip
  , OLD.isActive
  , OLD.codeConcentrationUnit
  , OLD.codeApplication
  , OLD.protocolDescription
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For Chromatogram 
--

-- select 'Creating table Chromatogram'$$

-- DROP TABLE IF EXISTS `Chromatogram_Audit`$$

CREATE TABLE IF NOT EXISTS `Chromatogram_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idChromatogram`  int(10)  NULL DEFAULT NULL
 ,`idPlateWell`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(200)  NULL DEFAULT NULL
 ,`readLength`  int(10)  NULL DEFAULT NULL
 ,`trimmedLength`  int(11)  NULL DEFAULT NULL
 ,`q20`  int(10)  NULL DEFAULT NULL
 ,`q40`  int(10)  NULL DEFAULT NULL
 ,`aSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`cSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`gSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`tSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`releaseDate`  datetime  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(500)  NULL DEFAULT NULL
 ,`idReleaser`  int(10)  NULL DEFAULT NULL
 ,`lane`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Chromatogram 
--

INSERT INTO Chromatogram_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idChromatogram
  , idPlateWell
  , idRequest
  , fileName
  , readLength
  , trimmedLength
  , q20
  , q40
  , aSignalStrength
  , cSignalStrength
  , gSignalStrength
  , tSignalStrength
  , releaseDate
  , qualifiedFilePath
  , idReleaser
  , lane )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idChromatogram
  , idPlateWell
  , idRequest
  , fileName
  , readLength
  , trimmedLength
  , q20
  , q40
  , aSignalStrength
  , cSignalStrength
  , gSignalStrength
  , tSignalStrength
  , releaseDate
  , qualifiedFilePath
  , idReleaser
  , lane
  FROM Chromatogram
  WHERE NOT EXISTS(SELECT * FROM Chromatogram_Audit)
$$

--
-- Audit Triggers For Chromatogram 
--


CREATE TRIGGER TrAI_Chromatogram_FER AFTER INSERT ON Chromatogram FOR EACH ROW
BEGIN
  INSERT INTO Chromatogram_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idChromatogram
  , idPlateWell
  , idRequest
  , fileName
  , readLength
  , trimmedLength
  , q20
  , q40
  , aSignalStrength
  , cSignalStrength
  , gSignalStrength
  , tSignalStrength
  , releaseDate
  , qualifiedFilePath
  , idReleaser
  , lane )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idChromatogram
  , NEW.idPlateWell
  , NEW.idRequest
  , NEW.fileName
  , NEW.readLength
  , NEW.trimmedLength
  , NEW.q20
  , NEW.q40
  , NEW.aSignalStrength
  , NEW.cSignalStrength
  , NEW.gSignalStrength
  , NEW.tSignalStrength
  , NEW.releaseDate
  , NEW.qualifiedFilePath
  , NEW.idReleaser
  , NEW.lane );
END;
$$


CREATE TRIGGER TrAU_Chromatogram_FER AFTER UPDATE ON Chromatogram FOR EACH ROW
BEGIN
  INSERT INTO Chromatogram_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idChromatogram
  , idPlateWell
  , idRequest
  , fileName
  , readLength
  , trimmedLength
  , q20
  , q40
  , aSignalStrength
  , cSignalStrength
  , gSignalStrength
  , tSignalStrength
  , releaseDate
  , qualifiedFilePath
  , idReleaser
  , lane )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idChromatogram
  , NEW.idPlateWell
  , NEW.idRequest
  , NEW.fileName
  , NEW.readLength
  , NEW.trimmedLength
  , NEW.q20
  , NEW.q40
  , NEW.aSignalStrength
  , NEW.cSignalStrength
  , NEW.gSignalStrength
  , NEW.tSignalStrength
  , NEW.releaseDate
  , NEW.qualifiedFilePath
  , NEW.idReleaser
  , NEW.lane );
END;
$$


CREATE TRIGGER TrAD_Chromatogram_FER AFTER DELETE ON Chromatogram FOR EACH ROW
BEGIN
  INSERT INTO Chromatogram_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idChromatogram
  , idPlateWell
  , idRequest
  , fileName
  , readLength
  , trimmedLength
  , q20
  , q40
  , aSignalStrength
  , cSignalStrength
  , gSignalStrength
  , tSignalStrength
  , releaseDate
  , qualifiedFilePath
  , idReleaser
  , lane )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idChromatogram
  , OLD.idPlateWell
  , OLD.idRequest
  , OLD.fileName
  , OLD.readLength
  , OLD.trimmedLength
  , OLD.q20
  , OLD.q40
  , OLD.aSignalStrength
  , OLD.cSignalStrength
  , OLD.gSignalStrength
  , OLD.tSignalStrength
  , OLD.releaseDate
  , OLD.qualifiedFilePath
  , OLD.idReleaser
  , OLD.lane );
END;
$$


--
-- Audit Table For ConcentrationUnit 
--

-- select 'Creating table ConcentrationUnit'$$

-- DROP TABLE IF EXISTS `ConcentrationUnit_Audit`$$

CREATE TABLE IF NOT EXISTS `ConcentrationUnit_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`concentrationUnit`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ConcentrationUnit 
--

INSERT INTO ConcentrationUnit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  FROM ConcentrationUnit
  WHERE NOT EXISTS(SELECT * FROM ConcentrationUnit_Audit)
$$

--
-- Audit Triggers For ConcentrationUnit 
--


CREATE TRIGGER TrAI_ConcentrationUnit_FER AFTER INSERT ON ConcentrationUnit FOR EACH ROW
BEGIN
  INSERT INTO ConcentrationUnit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeConcentrationUnit
  , NEW.concentrationUnit
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_ConcentrationUnit_FER AFTER UPDATE ON ConcentrationUnit FOR EACH ROW
BEGIN
  INSERT INTO ConcentrationUnit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeConcentrationUnit
  , NEW.concentrationUnit
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_ConcentrationUnit_FER AFTER DELETE ON ConcentrationUnit FOR EACH ROW
BEGIN
  INSERT INTO ConcentrationUnit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeConcentrationUnit
  , OLD.concentrationUnit
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive );
END;
$$


--
-- Audit Table For ContextSensitiveHelp 
--

-- select 'Creating table ContextSensitiveHelp'$$

-- DROP TABLE IF EXISTS `ContextSensitiveHelp_Audit`$$

CREATE TABLE IF NOT EXISTS `ContextSensitiveHelp_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idContextSensitiveHelp`  int(10)  NULL DEFAULT NULL
 ,`context1`  varchar(100)  NULL DEFAULT NULL
 ,`context2`  varchar(100)  NULL DEFAULT NULL
 ,`context3`  varchar(100)  NULL DEFAULT NULL
 ,`helpText`  varchar(10000)  NULL DEFAULT NULL
 ,`toolTipText`  varchar(10000)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ContextSensitiveHelp 
--

INSERT INTO ContextSensitiveHelp_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText
  FROM ContextSensitiveHelp
  WHERE NOT EXISTS(SELECT * FROM ContextSensitiveHelp_Audit)
$$

--
-- Audit Triggers For ContextSensitiveHelp 
--


CREATE TRIGGER TrAI_ContextSensitiveHelp_FER AFTER INSERT ON ContextSensitiveHelp FOR EACH ROW
BEGIN
  INSERT INTO ContextSensitiveHelp_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idContextSensitiveHelp
  , NEW.context1
  , NEW.context2
  , NEW.context3
  , NEW.helpText
  , NEW.toolTipText );
END;
$$


CREATE TRIGGER TrAU_ContextSensitiveHelp_FER AFTER UPDATE ON ContextSensitiveHelp FOR EACH ROW
BEGIN
  INSERT INTO ContextSensitiveHelp_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idContextSensitiveHelp
  , NEW.context1
  , NEW.context2
  , NEW.context3
  , NEW.helpText
  , NEW.toolTipText );
END;
$$


CREATE TRIGGER TrAD_ContextSensitiveHelp_FER AFTER DELETE ON ContextSensitiveHelp FOR EACH ROW
BEGIN
  INSERT INTO ContextSensitiveHelp_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idContextSensitiveHelp
  , OLD.context1
  , OLD.context2
  , OLD.context3
  , OLD.helpText
  , OLD.toolTipText );
END;
$$


--
-- Audit Table For CoreFacilityLab 
--

-- select 'Creating table CoreFacilityLab'$$

-- DROP TABLE IF EXISTS `CoreFacilityLab_Audit`$$

CREATE TABLE IF NOT EXISTS `CoreFacilityLab_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for CoreFacilityLab 
--

INSERT INTO CoreFacilityLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idLab )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idCoreFacility
  , idLab
  FROM CoreFacilityLab
  WHERE NOT EXISTS(SELECT * FROM CoreFacilityLab_Audit)
$$

--
-- Audit Triggers For CoreFacilityLab 
--


CREATE TRIGGER TrAI_CoreFacilityLab_FER AFTER INSERT ON CoreFacilityLab FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilityLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAU_CoreFacilityLab_FER AFTER UPDATE ON CoreFacilityLab FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilityLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAD_CoreFacilityLab_FER AFTER DELETE ON CoreFacilityLab FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilityLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idCoreFacility
  , OLD.idLab );
END;
$$


--
-- Audit Table For CoreFacilityManager 
--

-- select 'Creating table CoreFacilityManager'$$

-- DROP TABLE IF EXISTS `CoreFacilityManager_Audit`$$

CREATE TABLE IF NOT EXISTS `CoreFacilityManager_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for CoreFacilityManager 
--

INSERT INTO CoreFacilityManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idCoreFacility
  , idAppUser
  FROM CoreFacilityManager
  WHERE NOT EXISTS(SELECT * FROM CoreFacilityManager_Audit)
$$

--
-- Audit Triggers For CoreFacilityManager 
--


CREATE TRIGGER TrAI_CoreFacilityManager_FER AFTER INSERT ON CoreFacilityManager FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilityManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_CoreFacilityManager_FER AFTER UPDATE ON CoreFacilityManager FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilityManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_CoreFacilityManager_FER AFTER DELETE ON CoreFacilityManager FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilityManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idCoreFacility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For CoreFacilitySubmitter 
--

-- select 'Creating table CoreFacilitySubmitter'$$

-- DROP TABLE IF EXISTS `CoreFacilitySubmitter_Audit`$$

CREATE TABLE IF NOT EXISTS `CoreFacilitySubmitter_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for CoreFacilitySubmitter 
--

INSERT INTO CoreFacilitySubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idCoreFacility
  , idAppUser
  FROM CoreFacilitySubmitter
  WHERE NOT EXISTS(SELECT * FROM CoreFacilitySubmitter_Audit)
$$

--
-- Audit Triggers For CoreFacilitySubmitter 
--


CREATE TRIGGER TrAI_CoreFacilitySubmitter_FER AFTER INSERT ON CoreFacilitySubmitter FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilitySubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_CoreFacilitySubmitter_FER AFTER UPDATE ON CoreFacilitySubmitter FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilitySubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_CoreFacilitySubmitter_FER AFTER DELETE ON CoreFacilitySubmitter FOR EACH ROW
BEGIN
  INSERT INTO CoreFacilitySubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idCoreFacility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For CoreFacility 
--

-- select 'Creating table CoreFacility'$$

-- DROP TABLE IF EXISTS `CoreFacility_Audit`$$

CREATE TABLE IF NOT EXISTS `CoreFacility_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`facilityName`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`showProjectAnnotations`  char(1)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`acceptOnlineWorkAuth`  char(1)  NULL DEFAULT NULL
 ,`shortDescription`  varchar(1000)  NULL DEFAULT NULL
 ,`contactName`  varchar(200)  NULL DEFAULT NULL
 ,`contactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`contactPhone`  varchar(200)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`contactImage`  varchar(200)  NULL DEFAULT NULL
 ,`labPhone`  varchar(200)  NULL DEFAULT NULL
 ,`contactRoom`  varchar(200)  NULL DEFAULT NULL
 ,`labRoom`  varchar(200)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for CoreFacility 
--

INSERT INTO CoreFacility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , facilityName
  , isActive
  , showProjectAnnotations
  , description
  , acceptOnlineWorkAuth
  , shortDescription
  , contactName
  , contactEmail
  , contactPhone
  , sortOrder
  , contactImage
  , labPhone
  , contactRoom
  , labRoom )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idCoreFacility
  , facilityName
  , isActive
  , showProjectAnnotations
  , description
  , acceptOnlineWorkAuth
  , shortDescription
  , contactName
  , contactEmail
  , contactPhone
  , sortOrder
  , contactImage
  , labPhone
  , contactRoom
  , labRoom
  FROM CoreFacility
  WHERE NOT EXISTS(SELECT * FROM CoreFacility_Audit)
$$

--
-- Audit Triggers For CoreFacility 
--


CREATE TRIGGER TrAI_CoreFacility_FER AFTER INSERT ON CoreFacility FOR EACH ROW
BEGIN
  INSERT INTO CoreFacility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , facilityName
  , isActive
  , showProjectAnnotations
  , description
  , acceptOnlineWorkAuth
  , shortDescription
  , contactName
  , contactEmail
  , contactPhone
  , sortOrder
  , contactImage
  , labPhone
  , contactRoom
  , labRoom )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.facilityName
  , NEW.isActive
  , NEW.showProjectAnnotations
  , NEW.description
  , NEW.acceptOnlineWorkAuth
  , NEW.shortDescription
  , NEW.contactName
  , NEW.contactEmail
  , NEW.contactPhone
  , NEW.sortOrder
  , NEW.contactImage
  , NEW.labPhone
  , NEW.contactRoom
  , NEW.labRoom );
END;
$$


CREATE TRIGGER TrAU_CoreFacility_FER AFTER UPDATE ON CoreFacility FOR EACH ROW
BEGIN
  INSERT INTO CoreFacility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , facilityName
  , isActive
  , showProjectAnnotations
  , description
  , acceptOnlineWorkAuth
  , shortDescription
  , contactName
  , contactEmail
  , contactPhone
  , sortOrder
  , contactImage
  , labPhone
  , contactRoom
  , labRoom )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idCoreFacility
  , NEW.facilityName
  , NEW.isActive
  , NEW.showProjectAnnotations
  , NEW.description
  , NEW.acceptOnlineWorkAuth
  , NEW.shortDescription
  , NEW.contactName
  , NEW.contactEmail
  , NEW.contactPhone
  , NEW.sortOrder
  , NEW.contactImage
  , NEW.labPhone
  , NEW.contactRoom
  , NEW.labRoom );
END;
$$


CREATE TRIGGER TrAD_CoreFacility_FER AFTER DELETE ON CoreFacility FOR EACH ROW
BEGIN
  INSERT INTO CoreFacility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCoreFacility
  , facilityName
  , isActive
  , showProjectAnnotations
  , description
  , acceptOnlineWorkAuth
  , shortDescription
  , contactName
  , contactEmail
  , contactPhone
  , sortOrder
  , contactImage
  , labPhone
  , contactRoom
  , labRoom )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idCoreFacility
  , OLD.facilityName
  , OLD.isActive
  , OLD.showProjectAnnotations
  , OLD.description
  , OLD.acceptOnlineWorkAuth
  , OLD.shortDescription
  , OLD.contactName
  , OLD.contactEmail
  , OLD.contactPhone
  , OLD.sortOrder
  , OLD.contactImage
  , OLD.labPhone
  , OLD.contactRoom
  , OLD.labRoom );
END;
$$


--
-- Audit Table For CreditCardCompany 
--

-- select 'Creating table CreditCardCompany'$$

-- DROP TABLE IF EXISTS `CreditCardCompany_Audit`$$

CREATE TABLE IF NOT EXISTS `CreditCardCompany_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idCreditCardCompany`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  varchar(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for CreditCardCompany 
--

INSERT INTO CreditCardCompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder
  FROM CreditCardCompany
  WHERE NOT EXISTS(SELECT * FROM CreditCardCompany_Audit)
$$

--
-- Audit Triggers For CreditCardCompany 
--


CREATE TRIGGER TrAI_CreditCardCompany_FER AFTER INSERT ON CreditCardCompany FOR EACH ROW
BEGIN
  INSERT INTO CreditCardCompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idCreditCardCompany
  , NEW.name
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_CreditCardCompany_FER AFTER UPDATE ON CreditCardCompany FOR EACH ROW
BEGIN
  INSERT INTO CreditCardCompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idCreditCardCompany
  , NEW.name
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_CreditCardCompany_FER AFTER DELETE ON CreditCardCompany FOR EACH ROW
BEGIN
  INSERT INTO CreditCardCompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idCreditCardCompany
  , OLD.name
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For DataTrackCollaborator 
--

-- select 'Creating table DataTrackCollaborator'$$

-- DROP TABLE IF EXISTS `DataTrackCollaborator_Audit`$$

CREATE TABLE IF NOT EXISTS `DataTrackCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for DataTrackCollaborator 
--

INSERT INTO DataTrackCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idDataTrack
  , idAppUser
  FROM DataTrackCollaborator
  WHERE NOT EXISTS(SELECT * FROM DataTrackCollaborator_Audit)
$$

--
-- Audit Triggers For DataTrackCollaborator 
--


CREATE TRIGGER TrAI_DataTrackCollaborator_FER AFTER INSERT ON DataTrackCollaborator FOR EACH ROW
BEGIN
  INSERT INTO DataTrackCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrack
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_DataTrackCollaborator_FER AFTER UPDATE ON DataTrackCollaborator FOR EACH ROW
BEGIN
  INSERT INTO DataTrackCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrack
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_DataTrackCollaborator_FER AFTER DELETE ON DataTrackCollaborator FOR EACH ROW
BEGIN
  INSERT INTO DataTrackCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idDataTrack
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For DataTrackFile 
--

-- select 'Creating table DataTrackFile'$$

-- DROP TABLE IF EXISTS `DataTrackFile_Audit`$$

CREATE TABLE IF NOT EXISTS `DataTrackFile_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idDataTrackFile`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for DataTrackFile 
--

INSERT INTO DataTrackFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack
  FROM DataTrackFile
  WHERE NOT EXISTS(SELECT * FROM DataTrackFile_Audit)
$$

--
-- Audit Triggers For DataTrackFile 
--


CREATE TRIGGER TrAI_DataTrackFile_FER AFTER INSERT ON DataTrackFile FOR EACH ROW
BEGIN
  INSERT INTO DataTrackFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrackFile
  , NEW.idAnalysisFile
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAU_DataTrackFile_FER AFTER UPDATE ON DataTrackFile FOR EACH ROW
BEGIN
  INSERT INTO DataTrackFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrackFile
  , NEW.idAnalysisFile
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAD_DataTrackFile_FER AFTER DELETE ON DataTrackFile FOR EACH ROW
BEGIN
  INSERT INTO DataTrackFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idDataTrackFile
  , OLD.idAnalysisFile
  , OLD.idDataTrack );
END;
$$


--
-- Audit Table For DataTrackFolder 
--

-- select 'Creating table DataTrackFolder'$$

-- DROP TABLE IF EXISTS `DataTrackFolder_Audit`$$

CREATE TABLE IF NOT EXISTS `DataTrackFolder_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`idParentDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for DataTrackFolder 
--

INSERT INTO DataTrackFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate
  FROM DataTrackFolder
  WHERE NOT EXISTS(SELECT * FROM DataTrackFolder_Audit)
$$

--
-- Audit Triggers For DataTrackFolder 
--


CREATE TRIGGER TrAI_DataTrackFolder_FER AFTER INSERT ON DataTrackFolder FOR EACH ROW
BEGIN
  INSERT INTO DataTrackFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrackFolder
  , NEW.name
  , NEW.description
  , NEW.idParentDataTrackFolder
  , NEW.idGenomeBuild
  , NEW.idLab
  , NEW.createdBy
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAU_DataTrackFolder_FER AFTER UPDATE ON DataTrackFolder FOR EACH ROW
BEGIN
  INSERT INTO DataTrackFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrackFolder
  , NEW.name
  , NEW.description
  , NEW.idParentDataTrackFolder
  , NEW.idGenomeBuild
  , NEW.idLab
  , NEW.createdBy
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAD_DataTrackFolder_FER AFTER DELETE ON DataTrackFolder FOR EACH ROW
BEGIN
  INSERT INTO DataTrackFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idDataTrackFolder
  , OLD.name
  , OLD.description
  , OLD.idParentDataTrackFolder
  , OLD.idGenomeBuild
  , OLD.idLab
  , OLD.createdBy
  , OLD.createDate );
END;
$$


--
-- Audit Table For DataTrackToFolder 
--

-- select 'Creating table DataTrackToFolder'$$

-- DROP TABLE IF EXISTS `DataTrackToFolder_Audit`$$

CREATE TABLE IF NOT EXISTS `DataTrackToFolder_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for DataTrackToFolder 
--

INSERT INTO DataTrackToFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idDataTrackFolder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idDataTrack
  , idDataTrackFolder
  FROM DataTrackToFolder
  WHERE NOT EXISTS(SELECT * FROM DataTrackToFolder_Audit)
$$

--
-- Audit Triggers For DataTrackToFolder 
--


CREATE TRIGGER TrAI_DataTrackToFolder_FER AFTER INSERT ON DataTrackToFolder FOR EACH ROW
BEGIN
  INSERT INTO DataTrackToFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrack
  , NEW.idDataTrackFolder );
END;
$$


CREATE TRIGGER TrAU_DataTrackToFolder_FER AFTER UPDATE ON DataTrackToFolder FOR EACH ROW
BEGIN
  INSERT INTO DataTrackToFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrack
  , NEW.idDataTrackFolder );
END;
$$


CREATE TRIGGER TrAD_DataTrackToFolder_FER AFTER DELETE ON DataTrackToFolder FOR EACH ROW
BEGIN
  INSERT INTO DataTrackToFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idDataTrack
  , OLD.idDataTrackFolder );
END;
$$


--
-- Audit Table For DataTrackToTopic 
--

-- select 'Creating table DataTrackToTopic'$$

-- DROP TABLE IF EXISTS `DataTrackToTopic_Audit`$$

CREATE TABLE IF NOT EXISTS `DataTrackToTopic_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for DataTrackToTopic 
--

INSERT INTO DataTrackToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idDataTrack )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idTopic
  , idDataTrack
  FROM DataTrackToTopic
  WHERE NOT EXISTS(SELECT * FROM DataTrackToTopic_Audit)
$$

--
-- Audit Triggers For DataTrackToTopic 
--


CREATE TRIGGER TrAI_DataTrackToTopic_FER AFTER INSERT ON DataTrackToTopic FOR EACH ROW
BEGIN
  INSERT INTO DataTrackToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAU_DataTrackToTopic_FER AFTER UPDATE ON DataTrackToTopic FOR EACH ROW
BEGIN
  INSERT INTO DataTrackToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAD_DataTrackToTopic_FER AFTER DELETE ON DataTrackToTopic FOR EACH ROW
BEGIN
  INSERT INTO DataTrackToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idTopic
  , OLD.idDataTrack );
END;
$$


--
-- Audit Table For DataTrack 
--

-- select 'Creating table DataTrack'$$

-- DROP TABLE IF EXISTS `DataTrack_Audit`$$

CREATE TABLE IF NOT EXISTS `DataTrack_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`summary`  varchar(5000)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`isLoaded`  char(1)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`dataPath`  varchar(500)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for DataTrack 
--

INSERT INTO DataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , name
  , description
  , fileName
  , idGenomeBuild
  , codeVisibility
  , idAppUser
  , idLab
  , summary
  , createdBy
  , createDate
  , isLoaded
  , idInstitution
  , dataPath )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idDataTrack
  , name
  , description
  , fileName
  , idGenomeBuild
  , codeVisibility
  , idAppUser
  , idLab
  , summary
  , createdBy
  , createDate
  , isLoaded
  , idInstitution
  , dataPath
  FROM DataTrack
  WHERE NOT EXISTS(SELECT * FROM DataTrack_Audit)
$$

--
-- Audit Triggers For DataTrack 
--


CREATE TRIGGER TrAI_DataTrack_FER AFTER INSERT ON DataTrack FOR EACH ROW
BEGIN
  INSERT INTO DataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , name
  , description
  , fileName
  , idGenomeBuild
  , codeVisibility
  , idAppUser
  , idLab
  , summary
  , createdBy
  , createDate
  , isLoaded
  , idInstitution
  , dataPath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrack
  , NEW.name
  , NEW.description
  , NEW.fileName
  , NEW.idGenomeBuild
  , NEW.codeVisibility
  , NEW.idAppUser
  , NEW.idLab
  , NEW.summary
  , NEW.createdBy
  , NEW.createDate
  , NEW.isLoaded
  , NEW.idInstitution
  , NEW.dataPath );
END;
$$


CREATE TRIGGER TrAU_DataTrack_FER AFTER UPDATE ON DataTrack FOR EACH ROW
BEGIN
  INSERT INTO DataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , name
  , description
  , fileName
  , idGenomeBuild
  , codeVisibility
  , idAppUser
  , idLab
  , summary
  , createdBy
  , createDate
  , isLoaded
  , idInstitution
  , dataPath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idDataTrack
  , NEW.name
  , NEW.description
  , NEW.fileName
  , NEW.idGenomeBuild
  , NEW.codeVisibility
  , NEW.idAppUser
  , NEW.idLab
  , NEW.summary
  , NEW.createdBy
  , NEW.createDate
  , NEW.isLoaded
  , NEW.idInstitution
  , NEW.dataPath );
END;
$$


CREATE TRIGGER TrAD_DataTrack_FER AFTER DELETE ON DataTrack FOR EACH ROW
BEGIN
  INSERT INTO DataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDataTrack
  , name
  , description
  , fileName
  , idGenomeBuild
  , codeVisibility
  , idAppUser
  , idLab
  , summary
  , createdBy
  , createDate
  , isLoaded
  , idInstitution
  , dataPath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idDataTrack
  , OLD.name
  , OLD.description
  , OLD.fileName
  , OLD.idGenomeBuild
  , OLD.codeVisibility
  , OLD.idAppUser
  , OLD.idLab
  , OLD.summary
  , OLD.createdBy
  , OLD.createDate
  , OLD.isLoaded
  , OLD.idInstitution
  , OLD.dataPath );
END;
$$


--
-- Audit Table For DiskUsageByMonth 
--

-- select 'Creating table DiskUsageByMonth'$$

-- DROP TABLE IF EXISTS `DiskUsageByMonth_Audit`$$

CREATE TABLE IF NOT EXISTS `DiskUsageByMonth_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idDiskUsageByMonth`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`asOfDate`  datetime  NULL DEFAULT NULL
 ,`lastCalcDate`  datetime  NULL DEFAULT NULL
 ,`totalAnalysisDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`assessedAnalysisDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`totalExperimentDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`assessedExperimentDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for DiskUsageByMonth 
--

INSERT INTO DiskUsageByMonth_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDiskUsageByMonth
  , idLab
  , asOfDate
  , lastCalcDate
  , totalAnalysisDiskSpace
  , assessedAnalysisDiskSpace
  , totalExperimentDiskSpace
  , assessedExperimentDiskSpace
  , idBillingPeriod
  , idBillingAccount
  , idCoreFacility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idDiskUsageByMonth
  , idLab
  , asOfDate
  , lastCalcDate
  , totalAnalysisDiskSpace
  , assessedAnalysisDiskSpace
  , totalExperimentDiskSpace
  , assessedExperimentDiskSpace
  , idBillingPeriod
  , idBillingAccount
  , idCoreFacility
  FROM DiskUsageByMonth
  WHERE NOT EXISTS(SELECT * FROM DiskUsageByMonth_Audit)
$$

--
-- Audit Triggers For DiskUsageByMonth 
--


CREATE TRIGGER TrAI_DiskUsageByMonth_FER AFTER INSERT ON DiskUsageByMonth FOR EACH ROW
BEGIN
  INSERT INTO DiskUsageByMonth_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDiskUsageByMonth
  , idLab
  , asOfDate
  , lastCalcDate
  , totalAnalysisDiskSpace
  , assessedAnalysisDiskSpace
  , totalExperimentDiskSpace
  , assessedExperimentDiskSpace
  , idBillingPeriod
  , idBillingAccount
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idDiskUsageByMonth
  , NEW.idLab
  , NEW.asOfDate
  , NEW.lastCalcDate
  , NEW.totalAnalysisDiskSpace
  , NEW.assessedAnalysisDiskSpace
  , NEW.totalExperimentDiskSpace
  , NEW.assessedExperimentDiskSpace
  , NEW.idBillingPeriod
  , NEW.idBillingAccount
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_DiskUsageByMonth_FER AFTER UPDATE ON DiskUsageByMonth FOR EACH ROW
BEGIN
  INSERT INTO DiskUsageByMonth_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDiskUsageByMonth
  , idLab
  , asOfDate
  , lastCalcDate
  , totalAnalysisDiskSpace
  , assessedAnalysisDiskSpace
  , totalExperimentDiskSpace
  , assessedExperimentDiskSpace
  , idBillingPeriod
  , idBillingAccount
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idDiskUsageByMonth
  , NEW.idLab
  , NEW.asOfDate
  , NEW.lastCalcDate
  , NEW.totalAnalysisDiskSpace
  , NEW.assessedAnalysisDiskSpace
  , NEW.totalExperimentDiskSpace
  , NEW.assessedExperimentDiskSpace
  , NEW.idBillingPeriod
  , NEW.idBillingAccount
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_DiskUsageByMonth_FER AFTER DELETE ON DiskUsageByMonth FOR EACH ROW
BEGIN
  INSERT INTO DiskUsageByMonth_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idDiskUsageByMonth
  , idLab
  , asOfDate
  , lastCalcDate
  , totalAnalysisDiskSpace
  , assessedAnalysisDiskSpace
  , totalExperimentDiskSpace
  , assessedExperimentDiskSpace
  , idBillingPeriod
  , idBillingAccount
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idDiskUsageByMonth
  , OLD.idLab
  , OLD.asOfDate
  , OLD.lastCalcDate
  , OLD.totalAnalysisDiskSpace
  , OLD.assessedAnalysisDiskSpace
  , OLD.totalExperimentDiskSpace
  , OLD.assessedExperimentDiskSpace
  , OLD.idBillingPeriod
  , OLD.idBillingAccount
  , OLD.idCoreFacility );
END;
$$



--
-- Audit Table For ExperimentDesignEntry 
--

-- select 'Creating table ExperimentDesignEntry'$$

-- DROP TABLE IF EXISTS `ExperimentDesignEntry_Audit`$$

CREATE TABLE IF NOT EXISTS `ExperimentDesignEntry_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idExperimentDesignEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ExperimentDesignEntry 
--

INSERT INTO ExperimentDesignEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel
  FROM ExperimentDesignEntry
  WHERE NOT EXISTS(SELECT * FROM ExperimentDesignEntry_Audit)
$$

--
-- Audit Triggers For ExperimentDesignEntry 
--


CREATE TRIGGER TrAI_ExperimentDesignEntry_FER AFTER INSERT ON ExperimentDesignEntry FOR EACH ROW
BEGIN
  INSERT INTO ExperimentDesignEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idExperimentDesignEntry
  , NEW.codeExperimentDesign
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_ExperimentDesignEntry_FER AFTER UPDATE ON ExperimentDesignEntry FOR EACH ROW
BEGIN
  INSERT INTO ExperimentDesignEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idExperimentDesignEntry
  , NEW.codeExperimentDesign
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_ExperimentDesignEntry_FER AFTER DELETE ON ExperimentDesignEntry FOR EACH ROW
BEGIN
  INSERT INTO ExperimentDesignEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idExperimentDesignEntry
  , OLD.codeExperimentDesign
  , OLD.idProject
  , OLD.valueString
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For ExperimentDesign 
--

-- select 'Creating table ExperimentDesign'$$

-- DROP TABLE IF EXISTS `ExperimentDesign_Audit`$$

CREATE TABLE IF NOT EXISTS `ExperimentDesign_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`experimentDesign`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ExperimentDesign 
--

INSERT INTO ExperimentDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  FROM ExperimentDesign
  WHERE NOT EXISTS(SELECT * FROM ExperimentDesign_Audit)
$$

--
-- Audit Triggers For ExperimentDesign 
--


CREATE TRIGGER TrAI_ExperimentDesign_FER AFTER INSERT ON ExperimentDesign FOR EACH ROW
BEGIN
  INSERT INTO ExperimentDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeExperimentDesign
  , NEW.experimentDesign
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_ExperimentDesign_FER AFTER UPDATE ON ExperimentDesign FOR EACH ROW
BEGIN
  INSERT INTO ExperimentDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeExperimentDesign
  , NEW.experimentDesign
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_ExperimentDesign_FER AFTER DELETE ON ExperimentDesign FOR EACH ROW
BEGIN
  INSERT INTO ExperimentDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeExperimentDesign
  , OLD.experimentDesign
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For ExperimentFactorEntry 
--

-- select 'Creating table ExperimentFactorEntry'$$

-- DROP TABLE IF EXISTS `ExperimentFactorEntry_Audit`$$

CREATE TABLE IF NOT EXISTS `ExperimentFactorEntry_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idExperimentFactorEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ExperimentFactorEntry 
--

INSERT INTO ExperimentFactorEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel
  FROM ExperimentFactorEntry
  WHERE NOT EXISTS(SELECT * FROM ExperimentFactorEntry_Audit)
$$

--
-- Audit Triggers For ExperimentFactorEntry 
--


CREATE TRIGGER TrAI_ExperimentFactorEntry_FER AFTER INSERT ON ExperimentFactorEntry FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFactorEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idExperimentFactorEntry
  , NEW.codeExperimentFactor
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_ExperimentFactorEntry_FER AFTER UPDATE ON ExperimentFactorEntry FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFactorEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idExperimentFactorEntry
  , NEW.codeExperimentFactor
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_ExperimentFactorEntry_FER AFTER DELETE ON ExperimentFactorEntry FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFactorEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idExperimentFactorEntry
  , OLD.codeExperimentFactor
  , OLD.idProject
  , OLD.valueString
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For ExperimentFactor 
--

-- select 'Creating table ExperimentFactor'$$

-- DROP TABLE IF EXISTS `ExperimentFactor_Audit`$$

CREATE TABLE IF NOT EXISTS `ExperimentFactor_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`experimentFactor`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ExperimentFactor 
--

INSERT INTO ExperimentFactor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  FROM ExperimentFactor
  WHERE NOT EXISTS(SELECT * FROM ExperimentFactor_Audit)
$$

--
-- Audit Triggers For ExperimentFactor 
--


CREATE TRIGGER TrAI_ExperimentFactor_FER AFTER INSERT ON ExperimentFactor FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFactor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeExperimentFactor
  , NEW.experimentFactor
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_ExperimentFactor_FER AFTER UPDATE ON ExperimentFactor FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFactor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeExperimentFactor
  , NEW.experimentFactor
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_ExperimentFactor_FER AFTER DELETE ON ExperimentFactor FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFactor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeExperimentFactor
  , OLD.experimentFactor
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For ExperimentFile 
--

-- select 'Creating table ExperimentFile'$$

-- DROP TABLE IF EXISTS `ExperimentFile_Audit`$$

CREATE TABLE IF NOT EXISTS `ExperimentFile_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idExperimentFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ExperimentFile 
--

INSERT INTO ExperimentFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate
  FROM ExperimentFile
  WHERE NOT EXISTS(SELECT * FROM ExperimentFile_Audit)
$$

--
-- Audit Triggers For ExperimentFile 
--


CREATE TRIGGER TrAI_ExperimentFile_FER AFTER INSERT ON ExperimentFile FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idExperimentFile
  , NEW.fileName
  , NEW.fileSize
  , NEW.idRequest
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAU_ExperimentFile_FER AFTER UPDATE ON ExperimentFile FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idExperimentFile
  , NEW.fileName
  , NEW.fileSize
  , NEW.idRequest
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAD_ExperimentFile_FER AFTER DELETE ON ExperimentFile FOR EACH ROW
BEGIN
  INSERT INTO ExperimentFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idExperimentFile
  , OLD.fileName
  , OLD.fileSize
  , OLD.idRequest
  , OLD.createDate );
END;
$$


--
-- Audit Table For Faq 
--

-- select 'Creating table Faq'$$

-- DROP TABLE IF EXISTS `Faq_Audit`$$

CREATE TABLE IF NOT EXISTS `Faq_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idFAQ`  int(10)  NULL DEFAULT NULL
 ,`title`  varchar(300)  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Faq 
--

INSERT INTO Faq_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFAQ
  , title
  , url
  , idCoreFacility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idFAQ
  , title
  , url
  , idCoreFacility
  FROM Faq
  WHERE NOT EXISTS(SELECT * FROM Faq_Audit)
$$

--
-- Audit Triggers For Faq 
--


CREATE TRIGGER TrAI_Faq_FER AFTER INSERT ON Faq FOR EACH ROW
BEGIN
  INSERT INTO Faq_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idFAQ
  , NEW.title
  , NEW.url
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_Faq_FER AFTER UPDATE ON Faq FOR EACH ROW
BEGIN
  INSERT INTO Faq_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idFAQ
  , NEW.title
  , NEW.url
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_Faq_FER AFTER DELETE ON Faq FOR EACH ROW
BEGIN
  INSERT INTO Faq_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idFAQ
  , OLD.title
  , OLD.url
  , OLD.idCoreFacility );
END;
$$


--
-- Audit Table For FeatureExtractionProtocol 
--

-- select 'Creating table FeatureExtractionProtocol'$$

-- DROP TABLE IF EXISTS `FeatureExtractionProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `FeatureExtractionProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idFeatureExtractionProtocol`  int(10)  NULL DEFAULT NULL
 ,`featureExtractionProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for FeatureExtractionProtocol 
--

INSERT INTO FeatureExtractionProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive
  FROM FeatureExtractionProtocol
  WHERE NOT EXISTS(SELECT * FROM FeatureExtractionProtocol_Audit)
$$

--
-- Audit Triggers For FeatureExtractionProtocol 
--


CREATE TRIGGER TrAI_FeatureExtractionProtocol_FER AFTER INSERT ON FeatureExtractionProtocol FOR EACH ROW
BEGIN
  INSERT INTO FeatureExtractionProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idFeatureExtractionProtocol
  , NEW.featureExtractionProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_FeatureExtractionProtocol_FER AFTER UPDATE ON FeatureExtractionProtocol FOR EACH ROW
BEGIN
  INSERT INTO FeatureExtractionProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idFeatureExtractionProtocol
  , NEW.featureExtractionProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_FeatureExtractionProtocol_FER AFTER DELETE ON FeatureExtractionProtocol FOR EACH ROW
BEGIN
  INSERT INTO FeatureExtractionProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idFeatureExtractionProtocol
  , OLD.featureExtractionProtocol
  , OLD.codeRequestCategory
  , OLD.description
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For FlowCellChannel 
--

-- select 'Creating table FlowCellChannel'$$

-- DROP TABLE IF EXISTS `FlowCellChannel_Audit`$$

CREATE TABLE IF NOT EXISTS `FlowCellChannel_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`idFlowCell`  int(10)  NULL DEFAULT NULL
 ,`number`  int(10)  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idSequencingControl`  int(10)  NULL DEFAULT NULL
 ,`numberSequencingCyclesActual`  int(10)  NULL DEFAULT NULL
 ,`clustersPerTile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(500)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`firstCycleDate`  datetime  NULL DEFAULT NULL
 ,`firstCycleCompleted`  char(1)  NULL DEFAULT NULL
 ,`firstCycleFailed`  char(1)  NULL DEFAULT NULL
 ,`lastCycleDate`  datetime  NULL DEFAULT NULL
 ,`lastCycleCompleted`  char(1)  NULL DEFAULT NULL
 ,`lastCycleFailed`  char(1)  NULL DEFAULT NULL
 ,`sampleConcentrationpM`  decimal(8,2)  NULL DEFAULT NULL
 ,`pipelineDate`  datetime  NULL DEFAULT NULL
 ,`pipelineFailed`  char(1)  NULL DEFAULT NULL
 ,`isControl`  char(1)  NULL DEFAULT NULL
 ,`phiXErrorRate`  decimal(4,4)  NULL DEFAULT NULL
 ,`read1ClustersPassedFilterM`  decimal(6,2)  NULL DEFAULT NULL
 ,`read2ClustersPassedFilterM`  int(10)  NULL DEFAULT NULL
 ,`q30Gb`  decimal(4,1)  NULL DEFAULT NULL
 ,`q30Percent`  decimal(4,3)  NULL DEFAULT NULL
 ,`idPipelineProtocol`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for FlowCellChannel 
--

INSERT INTO FlowCellChannel_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCellChannel
  , idFlowCell
  , number
  , idSequenceLane
  , idSequencingControl
  , numberSequencingCyclesActual
  , clustersPerTile
  , fileName
  , startDate
  , firstCycleDate
  , firstCycleCompleted
  , firstCycleFailed
  , lastCycleDate
  , lastCycleCompleted
  , lastCycleFailed
  , sampleConcentrationpM
  , pipelineDate
  , pipelineFailed
  , isControl
  , phiXErrorRate
  , read1ClustersPassedFilterM
  , read2ClustersPassedFilterM
  , q30Gb
  , q30Percent
  , idPipelineProtocol )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idFlowCellChannel
  , idFlowCell
  , number
  , idSequenceLane
  , idSequencingControl
  , numberSequencingCyclesActual
  , clustersPerTile
  , fileName
  , startDate
  , firstCycleDate
  , firstCycleCompleted
  , firstCycleFailed
  , lastCycleDate
  , lastCycleCompleted
  , lastCycleFailed
  , sampleConcentrationpM
  , pipelineDate
  , pipelineFailed
  , isControl
  , phiXErrorRate
  , read1ClustersPassedFilterM
  , read2ClustersPassedFilterM
  , q30Gb
  , q30Percent
  , idPipelineProtocol
  FROM FlowCellChannel
  WHERE NOT EXISTS(SELECT * FROM FlowCellChannel_Audit)
$$

--
-- Audit Triggers For FlowCellChannel 
--


CREATE TRIGGER TrAI_FlowCellChannel_FER AFTER INSERT ON FlowCellChannel FOR EACH ROW
BEGIN
  INSERT INTO FlowCellChannel_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCellChannel
  , idFlowCell
  , number
  , idSequenceLane
  , idSequencingControl
  , numberSequencingCyclesActual
  , clustersPerTile
  , fileName
  , startDate
  , firstCycleDate
  , firstCycleCompleted
  , firstCycleFailed
  , lastCycleDate
  , lastCycleCompleted
  , lastCycleFailed
  , sampleConcentrationpM
  , pipelineDate
  , pipelineFailed
  , isControl
  , phiXErrorRate
  , read1ClustersPassedFilterM
  , read2ClustersPassedFilterM
  , q30Gb
  , q30Percent
  , idPipelineProtocol )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idFlowCellChannel
  , NEW.idFlowCell
  , NEW.number
  , NEW.idSequenceLane
  , NEW.idSequencingControl
  , NEW.numberSequencingCyclesActual
  , NEW.clustersPerTile
  , NEW.fileName
  , NEW.startDate
  , NEW.firstCycleDate
  , NEW.firstCycleCompleted
  , NEW.firstCycleFailed
  , NEW.lastCycleDate
  , NEW.lastCycleCompleted
  , NEW.lastCycleFailed
  , NEW.sampleConcentrationpM
  , NEW.pipelineDate
  , NEW.pipelineFailed
  , NEW.isControl
  , NEW.phiXErrorRate
  , NEW.read1ClustersPassedFilterM
  , NEW.read2ClustersPassedFilterM
  , NEW.q30Gb
  , NEW.q30Percent
  , NEW.idPipelineProtocol );
END;
$$


CREATE TRIGGER TrAU_FlowCellChannel_FER AFTER UPDATE ON FlowCellChannel FOR EACH ROW
BEGIN
  INSERT INTO FlowCellChannel_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCellChannel
  , idFlowCell
  , number
  , idSequenceLane
  , idSequencingControl
  , numberSequencingCyclesActual
  , clustersPerTile
  , fileName
  , startDate
  , firstCycleDate
  , firstCycleCompleted
  , firstCycleFailed
  , lastCycleDate
  , lastCycleCompleted
  , lastCycleFailed
  , sampleConcentrationpM
  , pipelineDate
  , pipelineFailed
  , isControl
  , phiXErrorRate
  , read1ClustersPassedFilterM
  , read2ClustersPassedFilterM
  , q30Gb
  , q30Percent
  , idPipelineProtocol )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idFlowCellChannel
  , NEW.idFlowCell
  , NEW.number
  , NEW.idSequenceLane
  , NEW.idSequencingControl
  , NEW.numberSequencingCyclesActual
  , NEW.clustersPerTile
  , NEW.fileName
  , NEW.startDate
  , NEW.firstCycleDate
  , NEW.firstCycleCompleted
  , NEW.firstCycleFailed
  , NEW.lastCycleDate
  , NEW.lastCycleCompleted
  , NEW.lastCycleFailed
  , NEW.sampleConcentrationpM
  , NEW.pipelineDate
  , NEW.pipelineFailed
  , NEW.isControl
  , NEW.phiXErrorRate
  , NEW.read1ClustersPassedFilterM
  , NEW.read2ClustersPassedFilterM
  , NEW.q30Gb
  , NEW.q30Percent
  , NEW.idPipelineProtocol );
END;
$$


CREATE TRIGGER TrAD_FlowCellChannel_FER AFTER DELETE ON FlowCellChannel FOR EACH ROW
BEGIN
  INSERT INTO FlowCellChannel_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCellChannel
  , idFlowCell
  , number
  , idSequenceLane
  , idSequencingControl
  , numberSequencingCyclesActual
  , clustersPerTile
  , fileName
  , startDate
  , firstCycleDate
  , firstCycleCompleted
  , firstCycleFailed
  , lastCycleDate
  , lastCycleCompleted
  , lastCycleFailed
  , sampleConcentrationpM
  , pipelineDate
  , pipelineFailed
  , isControl
  , phiXErrorRate
  , read1ClustersPassedFilterM
  , read2ClustersPassedFilterM
  , q30Gb
  , q30Percent
  , idPipelineProtocol )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idFlowCellChannel
  , OLD.idFlowCell
  , OLD.number
  , OLD.idSequenceLane
  , OLD.idSequencingControl
  , OLD.numberSequencingCyclesActual
  , OLD.clustersPerTile
  , OLD.fileName
  , OLD.startDate
  , OLD.firstCycleDate
  , OLD.firstCycleCompleted
  , OLD.firstCycleFailed
  , OLD.lastCycleDate
  , OLD.lastCycleCompleted
  , OLD.lastCycleFailed
  , OLD.sampleConcentrationpM
  , OLD.pipelineDate
  , OLD.pipelineFailed
  , OLD.isControl
  , OLD.phiXErrorRate
  , OLD.read1ClustersPassedFilterM
  , OLD.read2ClustersPassedFilterM
  , OLD.q30Gb
  , OLD.q30Percent
  , OLD.idPipelineProtocol );
END;
$$


--
-- Audit Table For FlowCell 
--

-- select 'Creating table FlowCell'$$

-- DROP TABLE IF EXISTS `FlowCell_Audit`$$

CREATE TABLE IF NOT EXISTS `FlowCell_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idFlowCell`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`notes`  varchar(200)  NULL DEFAULT NULL
 ,`barcode`  varchar(100)  NULL DEFAULT NULL
 ,`codeSequencingPlatform`  varchar(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
 ,`runNumber`  int(10)  NULL DEFAULT NULL
 ,`idInstrument`  int(10)  NULL DEFAULT NULL
 ,`side`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for FlowCell 
--

INSERT INTO FlowCell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCell
  , idNumberSequencingCycles
  , idSeqRunType
  , number
  , createDate
  , notes
  , barcode
  , codeSequencingPlatform
  , idCoreFacility
  , idNumberSequencingCyclesAllowed
  , runNumber
  , idInstrument
  , side )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idFlowCell
  , idNumberSequencingCycles
  , idSeqRunType
  , number
  , createDate
  , notes
  , barcode
  , codeSequencingPlatform
  , idCoreFacility
  , idNumberSequencingCyclesAllowed
  , runNumber
  , idInstrument
  , side
  FROM FlowCell
  WHERE NOT EXISTS(SELECT * FROM FlowCell_Audit)
$$

--
-- Audit Triggers For FlowCell 
--


CREATE TRIGGER TrAI_FlowCell_FER AFTER INSERT ON FlowCell FOR EACH ROW
BEGIN
  INSERT INTO FlowCell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCell
  , idNumberSequencingCycles
  , idSeqRunType
  , number
  , createDate
  , notes
  , barcode
  , codeSequencingPlatform
  , idCoreFacility
  , idNumberSequencingCyclesAllowed
  , runNumber
  , idInstrument
  , side )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idFlowCell
  , NEW.idNumberSequencingCycles
  , NEW.idSeqRunType
  , NEW.number
  , NEW.createDate
  , NEW.notes
  , NEW.barcode
  , NEW.codeSequencingPlatform
  , NEW.idCoreFacility
  , NEW.idNumberSequencingCyclesAllowed
  , NEW.runNumber
  , NEW.idInstrument
  , NEW.side );
END;
$$


CREATE TRIGGER TrAU_FlowCell_FER AFTER UPDATE ON FlowCell FOR EACH ROW
BEGIN
  INSERT INTO FlowCell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCell
  , idNumberSequencingCycles
  , idSeqRunType
  , number
  , createDate
  , notes
  , barcode
  , codeSequencingPlatform
  , idCoreFacility
  , idNumberSequencingCyclesAllowed
  , runNumber
  , idInstrument
  , side )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idFlowCell
  , NEW.idNumberSequencingCycles
  , NEW.idSeqRunType
  , NEW.number
  , NEW.createDate
  , NEW.notes
  , NEW.barcode
  , NEW.codeSequencingPlatform
  , NEW.idCoreFacility
  , NEW.idNumberSequencingCyclesAllowed
  , NEW.runNumber
  , NEW.idInstrument
  , NEW.side );
END;
$$


CREATE TRIGGER TrAD_FlowCell_FER AFTER DELETE ON FlowCell FOR EACH ROW
BEGIN
  INSERT INTO FlowCell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFlowCell
  , idNumberSequencingCycles
  , idSeqRunType
  , number
  , createDate
  , notes
  , barcode
  , codeSequencingPlatform
  , idCoreFacility
  , idNumberSequencingCyclesAllowed
  , runNumber
  , idInstrument
  , side )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idFlowCell
  , OLD.idNumberSequencingCycles
  , OLD.idSeqRunType
  , OLD.number
  , OLD.createDate
  , OLD.notes
  , OLD.barcode
  , OLD.codeSequencingPlatform
  , OLD.idCoreFacility
  , OLD.idNumberSequencingCyclesAllowed
  , OLD.runNumber
  , OLD.idInstrument
  , OLD.side );
END;
$$


--
-- Audit Table For FundingAgency 
--

-- select 'Creating table FundingAgency'$$

-- DROP TABLE IF EXISTS `FundingAgency_Audit`$$

CREATE TABLE IF NOT EXISTS `FundingAgency_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idFundingAgency`  int(10)  NULL DEFAULT NULL
 ,`fundingAgency`  varchar(200)  NULL DEFAULT NULL
 ,`isPeerReviewedFunding`  char(1)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for FundingAgency 
--

INSERT INTO FundingAgency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive
  FROM FundingAgency
  WHERE NOT EXISTS(SELECT * FROM FundingAgency_Audit)
$$

--
-- Audit Triggers For FundingAgency 
--


CREATE TRIGGER TrAI_FundingAgency_FER AFTER INSERT ON FundingAgency FOR EACH ROW
BEGIN
  INSERT INTO FundingAgency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idFundingAgency
  , NEW.fundingAgency
  , NEW.isPeerReviewedFunding
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_FundingAgency_FER AFTER UPDATE ON FundingAgency FOR EACH ROW
BEGIN
  INSERT INTO FundingAgency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idFundingAgency
  , NEW.fundingAgency
  , NEW.isPeerReviewedFunding
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_FundingAgency_FER AFTER DELETE ON FundingAgency FOR EACH ROW
BEGIN
  INSERT INTO FundingAgency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idFundingAgency
  , OLD.fundingAgency
  , OLD.isPeerReviewedFunding
  , OLD.isActive );
END;
$$


--
-- Audit Table For GenomeBuildAlias 
--

-- select 'Creating table GenomeBuildAlias'$$

-- DROP TABLE IF EXISTS `GenomeBuildAlias_Audit`$$

CREATE TABLE IF NOT EXISTS `GenomeBuildAlias_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idGenomeBuildAlias`  int(10)  NULL DEFAULT NULL
 ,`alias`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for GenomeBuildAlias 
--

INSERT INTO GenomeBuildAlias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild
  FROM GenomeBuildAlias
  WHERE NOT EXISTS(SELECT * FROM GenomeBuildAlias_Audit)
$$

--
-- Audit Triggers For GenomeBuildAlias 
--


CREATE TRIGGER TrAI_GenomeBuildAlias_FER AFTER INSERT ON GenomeBuildAlias FOR EACH ROW
BEGIN
  INSERT INTO GenomeBuildAlias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idGenomeBuildAlias
  , NEW.alias
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAU_GenomeBuildAlias_FER AFTER UPDATE ON GenomeBuildAlias FOR EACH ROW
BEGIN
  INSERT INTO GenomeBuildAlias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idGenomeBuildAlias
  , NEW.alias
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAD_GenomeBuildAlias_FER AFTER DELETE ON GenomeBuildAlias FOR EACH ROW
BEGIN
  INSERT INTO GenomeBuildAlias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idGenomeBuildAlias
  , OLD.alias
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For GenomeBuild 
--

-- select 'Creating table GenomeBuild'$$

-- DROP TABLE IF EXISTS `GenomeBuild_Audit`$$

CREATE TABLE IF NOT EXISTS `GenomeBuild_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`genomeBuildName`  varchar(500)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`isLatestBuild`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`das2Name`  varchar(200)  NULL DEFAULT NULL
 ,`buildDate`  datetime  NULL DEFAULT NULL
 ,`coordURI`  varchar(2000)  NULL DEFAULT NULL
 ,`coordVersion`  varchar(50)  NULL DEFAULT NULL
 ,`coordSource`  varchar(50)  NULL DEFAULT NULL
 ,`coordTestRange`  varchar(100)  NULL DEFAULT NULL
 ,`coordAuthority`  varchar(50)  NULL DEFAULT NULL
 ,`ucscName`  varchar(100)  NULL DEFAULT NULL
 ,`igvName`  varchar(50)  NULL DEFAULT NULL
 ,`dataPath`  varchar(500)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for GenomeBuild 
--

INSERT INTO GenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuild
  , genomeBuildName
  , idOrganism
  , isActive
  , isLatestBuild
  , idAppUser
  , das2Name
  , buildDate
  , coordURI
  , coordVersion
  , coordSource
  , coordTestRange
  , coordAuthority
  , ucscName
  , igvName
  , dataPath )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idGenomeBuild
  , genomeBuildName
  , idOrganism
  , isActive
  , isLatestBuild
  , idAppUser
  , das2Name
  , buildDate
  , coordURI
  , coordVersion
  , coordSource
  , coordTestRange
  , coordAuthority
  , ucscName
  , igvName
  , dataPath
  FROM GenomeBuild
  WHERE NOT EXISTS(SELECT * FROM GenomeBuild_Audit)
$$

--
-- Audit Triggers For GenomeBuild 
--


CREATE TRIGGER TrAI_GenomeBuild_FER AFTER INSERT ON GenomeBuild FOR EACH ROW
BEGIN
  INSERT INTO GenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuild
  , genomeBuildName
  , idOrganism
  , isActive
  , isLatestBuild
  , idAppUser
  , das2Name
  , buildDate
  , coordURI
  , coordVersion
  , coordSource
  , coordTestRange
  , coordAuthority
  , ucscName
  , igvName
  , dataPath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idGenomeBuild
  , NEW.genomeBuildName
  , NEW.idOrganism
  , NEW.isActive
  , NEW.isLatestBuild
  , NEW.idAppUser
  , NEW.das2Name
  , NEW.buildDate
  , NEW.coordURI
  , NEW.coordVersion
  , NEW.coordSource
  , NEW.coordTestRange
  , NEW.coordAuthority
  , NEW.ucscName
  , NEW.igvName
  , NEW.dataPath );
END;
$$


CREATE TRIGGER TrAU_GenomeBuild_FER AFTER UPDATE ON GenomeBuild FOR EACH ROW
BEGIN
  INSERT INTO GenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuild
  , genomeBuildName
  , idOrganism
  , isActive
  , isLatestBuild
  , idAppUser
  , das2Name
  , buildDate
  , coordURI
  , coordVersion
  , coordSource
  , coordTestRange
  , coordAuthority
  , ucscName
  , igvName
  , dataPath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idGenomeBuild
  , NEW.genomeBuildName
  , NEW.idOrganism
  , NEW.isActive
  , NEW.isLatestBuild
  , NEW.idAppUser
  , NEW.das2Name
  , NEW.buildDate
  , NEW.coordURI
  , NEW.coordVersion
  , NEW.coordSource
  , NEW.coordTestRange
  , NEW.coordAuthority
  , NEW.ucscName
  , NEW.igvName
  , NEW.dataPath );
END;
$$


CREATE TRIGGER TrAD_GenomeBuild_FER AFTER DELETE ON GenomeBuild FOR EACH ROW
BEGIN
  INSERT INTO GenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeBuild
  , genomeBuildName
  , idOrganism
  , isActive
  , isLatestBuild
  , idAppUser
  , das2Name
  , buildDate
  , coordURI
  , coordVersion
  , coordSource
  , coordTestRange
  , coordAuthority
  , ucscName
  , igvName
  , dataPath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idGenomeBuild
  , OLD.genomeBuildName
  , OLD.idOrganism
  , OLD.isActive
  , OLD.isLatestBuild
  , OLD.idAppUser
  , OLD.das2Name
  , OLD.buildDate
  , OLD.coordURI
  , OLD.coordVersion
  , OLD.coordSource
  , OLD.coordTestRange
  , OLD.coordAuthority
  , OLD.ucscName
  , OLD.igvName
  , OLD.dataPath );
END;
$$


--
-- Audit Table For GenomeIndex 
--

-- select 'Creating table GenomeIndex'$$

-- DROP TABLE IF EXISTS `GenomeIndex_Audit`$$

CREATE TABLE IF NOT EXISTS `GenomeIndex_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
 ,`genomeIndexName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for GenomeIndex 
--

INSERT INTO GenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism
  FROM GenomeIndex
  WHERE NOT EXISTS(SELECT * FROM GenomeIndex_Audit)
$$

--
-- Audit Triggers For GenomeIndex 
--


CREATE TRIGGER TrAI_GenomeIndex_FER AFTER INSERT ON GenomeIndex FOR EACH ROW
BEGIN
  INSERT INTO GenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idGenomeIndex
  , NEW.genomeIndexName
  , NEW.webServiceName
  , NEW.isActive
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAU_GenomeIndex_FER AFTER UPDATE ON GenomeIndex FOR EACH ROW
BEGIN
  INSERT INTO GenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idGenomeIndex
  , NEW.genomeIndexName
  , NEW.webServiceName
  , NEW.isActive
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAD_GenomeIndex_FER AFTER DELETE ON GenomeIndex FOR EACH ROW
BEGIN
  INSERT INTO GenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idGenomeIndex
  , OLD.genomeIndexName
  , OLD.webServiceName
  , OLD.isActive
  , OLD.idOrganism );
END;
$$


--
-- Audit Table For HybProtocol 
--

-- select 'Creating table HybProtocol'$$

-- DROP TABLE IF EXISTS `HybProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `HybProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idHybProtocol`  int(10)  NULL DEFAULT NULL
 ,`hybProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for HybProtocol 
--

INSERT INTO HybProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive
  FROM HybProtocol
  WHERE NOT EXISTS(SELECT * FROM HybProtocol_Audit)
$$

--
-- Audit Triggers For HybProtocol 
--


CREATE TRIGGER TrAI_HybProtocol_FER AFTER INSERT ON HybProtocol FOR EACH ROW
BEGIN
  INSERT INTO HybProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idHybProtocol
  , NEW.hybProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_HybProtocol_FER AFTER UPDATE ON HybProtocol FOR EACH ROW
BEGIN
  INSERT INTO HybProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idHybProtocol
  , NEW.hybProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_HybProtocol_FER AFTER DELETE ON HybProtocol FOR EACH ROW
BEGIN
  INSERT INTO HybProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idHybProtocol
  , OLD.hybProtocol
  , OLD.codeRequestCategory
  , OLD.description
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For Hybridization 
--

-- select 'Creating table Hybridization'$$

-- DROP TABLE IF EXISTS `Hybridization_Audit`$$

CREATE TABLE IF NOT EXISTS `Hybridization_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`notes`  varchar(2000)  NULL DEFAULT NULL
 ,`codeSlideSource`  varchar(10)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocol`  int(10)  NULL DEFAULT NULL
 ,`idHybBuffer`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSampleChannel1`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSampleChannel2`  int(10)  NULL DEFAULT NULL
 ,`idSlide`  int(10)  NULL DEFAULT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocol`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocol`  int(10)  NULL DEFAULT NULL
 ,`hybDate`  datetime  NULL DEFAULT NULL
 ,`hybFailed`  char(1)  NULL DEFAULT NULL
 ,`hybBypassed`  char(1)  NULL DEFAULT NULL
 ,`extractionDate`  datetime  NULL DEFAULT NULL
 ,`extractionFailed`  char(1)  NULL DEFAULT NULL
 ,`extractionBypassed`  char(1)  NULL DEFAULT NULL
 ,`hasResults`  char(1)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Hybridization 
--

INSERT INTO Hybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybridization
  , number
  , notes
  , codeSlideSource
  , idSlideDesign
  , idHybProtocol
  , idHybBuffer
  , idLabeledSampleChannel1
  , idLabeledSampleChannel2
  , idSlide
  , idArrayCoordinate
  , idScanProtocol
  , idFeatureExtractionProtocol
  , hybDate
  , hybFailed
  , hybBypassed
  , extractionDate
  , extractionFailed
  , extractionBypassed
  , hasResults
  , createDate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idHybridization
  , number
  , notes
  , codeSlideSource
  , idSlideDesign
  , idHybProtocol
  , idHybBuffer
  , idLabeledSampleChannel1
  , idLabeledSampleChannel2
  , idSlide
  , idArrayCoordinate
  , idScanProtocol
  , idFeatureExtractionProtocol
  , hybDate
  , hybFailed
  , hybBypassed
  , extractionDate
  , extractionFailed
  , extractionBypassed
  , hasResults
  , createDate
  FROM Hybridization
  WHERE NOT EXISTS(SELECT * FROM Hybridization_Audit)
$$

--
-- Audit Triggers For Hybridization 
--


CREATE TRIGGER TrAI_Hybridization_FER AFTER INSERT ON Hybridization FOR EACH ROW
BEGIN
  INSERT INTO Hybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybridization
  , number
  , notes
  , codeSlideSource
  , idSlideDesign
  , idHybProtocol
  , idHybBuffer
  , idLabeledSampleChannel1
  , idLabeledSampleChannel2
  , idSlide
  , idArrayCoordinate
  , idScanProtocol
  , idFeatureExtractionProtocol
  , hybDate
  , hybFailed
  , hybBypassed
  , extractionDate
  , extractionFailed
  , extractionBypassed
  , hasResults
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idHybridization
  , NEW.number
  , NEW.notes
  , NEW.codeSlideSource
  , NEW.idSlideDesign
  , NEW.idHybProtocol
  , NEW.idHybBuffer
  , NEW.idLabeledSampleChannel1
  , NEW.idLabeledSampleChannel2
  , NEW.idSlide
  , NEW.idArrayCoordinate
  , NEW.idScanProtocol
  , NEW.idFeatureExtractionProtocol
  , NEW.hybDate
  , NEW.hybFailed
  , NEW.hybBypassed
  , NEW.extractionDate
  , NEW.extractionFailed
  , NEW.extractionBypassed
  , NEW.hasResults
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAU_Hybridization_FER AFTER UPDATE ON Hybridization FOR EACH ROW
BEGIN
  INSERT INTO Hybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybridization
  , number
  , notes
  , codeSlideSource
  , idSlideDesign
  , idHybProtocol
  , idHybBuffer
  , idLabeledSampleChannel1
  , idLabeledSampleChannel2
  , idSlide
  , idArrayCoordinate
  , idScanProtocol
  , idFeatureExtractionProtocol
  , hybDate
  , hybFailed
  , hybBypassed
  , extractionDate
  , extractionFailed
  , extractionBypassed
  , hasResults
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idHybridization
  , NEW.number
  , NEW.notes
  , NEW.codeSlideSource
  , NEW.idSlideDesign
  , NEW.idHybProtocol
  , NEW.idHybBuffer
  , NEW.idLabeledSampleChannel1
  , NEW.idLabeledSampleChannel2
  , NEW.idSlide
  , NEW.idArrayCoordinate
  , NEW.idScanProtocol
  , NEW.idFeatureExtractionProtocol
  , NEW.hybDate
  , NEW.hybFailed
  , NEW.hybBypassed
  , NEW.extractionDate
  , NEW.extractionFailed
  , NEW.extractionBypassed
  , NEW.hasResults
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAD_Hybridization_FER AFTER DELETE ON Hybridization FOR EACH ROW
BEGIN
  INSERT INTO Hybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idHybridization
  , number
  , notes
  , codeSlideSource
  , idSlideDesign
  , idHybProtocol
  , idHybBuffer
  , idLabeledSampleChannel1
  , idLabeledSampleChannel2
  , idSlide
  , idArrayCoordinate
  , idScanProtocol
  , idFeatureExtractionProtocol
  , hybDate
  , hybFailed
  , hybBypassed
  , extractionDate
  , extractionFailed
  , extractionBypassed
  , hasResults
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idHybridization
  , OLD.number
  , OLD.notes
  , OLD.codeSlideSource
  , OLD.idSlideDesign
  , OLD.idHybProtocol
  , OLD.idHybBuffer
  , OLD.idLabeledSampleChannel1
  , OLD.idLabeledSampleChannel2
  , OLD.idSlide
  , OLD.idArrayCoordinate
  , OLD.idScanProtocol
  , OLD.idFeatureExtractionProtocol
  , OLD.hybDate
  , OLD.hybFailed
  , OLD.hybBypassed
  , OLD.extractionDate
  , OLD.extractionFailed
  , OLD.extractionBypassed
  , OLD.hasResults
  , OLD.createDate );
END;
$$


--
-- Audit Table For InstitutionLab 
--

-- select 'Creating table InstitutionLab'$$

-- DROP TABLE IF EXISTS `InstitutionLab_Audit`$$

CREATE TABLE IF NOT EXISTS `InstitutionLab_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for InstitutionLab 
--

INSERT INTO InstitutionLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , idLab )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idInstitution
  , idLab
  FROM InstitutionLab
  WHERE NOT EXISTS(SELECT * FROM InstitutionLab_Audit)
$$

--
-- Audit Triggers For InstitutionLab 
--


CREATE TRIGGER TrAI_InstitutionLab_FER AFTER INSERT ON InstitutionLab FOR EACH ROW
BEGIN
  INSERT INTO InstitutionLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idInstitution
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAU_InstitutionLab_FER AFTER UPDATE ON InstitutionLab FOR EACH ROW
BEGIN
  INSERT INTO InstitutionLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idInstitution
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAD_InstitutionLab_FER AFTER DELETE ON InstitutionLab FOR EACH ROW
BEGIN
  INSERT INTO InstitutionLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idInstitution
  , OLD.idLab );
END;
$$


--
-- Audit Table For Institution 
--

-- select 'Creating table Institution'$$

-- DROP TABLE IF EXISTS `Institution_Audit`$$

CREATE TABLE IF NOT EXISTS `Institution_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`institution`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`isDefault`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Institution 
--

INSERT INTO Institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault
  FROM Institution
  WHERE NOT EXISTS(SELECT * FROM Institution_Audit)
$$

--
-- Audit Triggers For Institution 
--


CREATE TRIGGER TrAI_Institution_FER AFTER INSERT ON Institution FOR EACH ROW
BEGIN
  INSERT INTO Institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idInstitution
  , NEW.institution
  , NEW.description
  , NEW.isActive
  , NEW.isDefault );
END;
$$


CREATE TRIGGER TrAU_Institution_FER AFTER UPDATE ON Institution FOR EACH ROW
BEGIN
  INSERT INTO Institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idInstitution
  , NEW.institution
  , NEW.description
  , NEW.isActive
  , NEW.isDefault );
END;
$$


CREATE TRIGGER TrAD_Institution_FER AFTER DELETE ON Institution FOR EACH ROW
BEGIN
  INSERT INTO Institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idInstitution
  , OLD.institution
  , OLD.description
  , OLD.isActive
  , OLD.isDefault );
END;
$$


--
-- Audit Table For InstrumentRunStatus 
--

-- select 'Creating table InstrumentRunStatus'$$

-- DROP TABLE IF EXISTS `InstrumentRunStatus_Audit`$$

CREATE TABLE IF NOT EXISTS `InstrumentRunStatus_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`instrumentRunStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for InstrumentRunStatus 
--

INSERT INTO InstrumentRunStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive
  FROM InstrumentRunStatus
  WHERE NOT EXISTS(SELECT * FROM InstrumentRunStatus_Audit)
$$

--
-- Audit Triggers For InstrumentRunStatus 
--


CREATE TRIGGER TrAI_InstrumentRunStatus_FER AFTER INSERT ON InstrumentRunStatus FOR EACH ROW
BEGIN
  INSERT INTO InstrumentRunStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeInstrumentRunStatus
  , NEW.instrumentRunStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_InstrumentRunStatus_FER AFTER UPDATE ON InstrumentRunStatus FOR EACH ROW
BEGIN
  INSERT INTO InstrumentRunStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeInstrumentRunStatus
  , NEW.instrumentRunStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_InstrumentRunStatus_FER AFTER DELETE ON InstrumentRunStatus FOR EACH ROW
BEGIN
  INSERT INTO InstrumentRunStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeInstrumentRunStatus
  , OLD.instrumentRunStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For InstrumentRun 
--

-- select 'Creating table InstrumentRun'$$

-- DROP TABLE IF EXISTS `InstrumentRun_Audit`$$

CREATE TABLE IF NOT EXISTS `InstrumentRun_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idInstrumentRun`  int(10)  NULL DEFAULT NULL
 ,`runDate`  datetime  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`comments`  varchar(200)  NULL DEFAULT NULL
 ,`label`  varchar(100)  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`creator`  varchar(50)  NULL DEFAULT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for InstrumentRun 
--

INSERT INTO InstrumentRun_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrumentRun
  , runDate
  , createDate
  , codeInstrumentRunStatus
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idInstrumentRun
  , runDate
  , createDate
  , codeInstrumentRunStatus
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType
  FROM InstrumentRun
  WHERE NOT EXISTS(SELECT * FROM InstrumentRun_Audit)
$$

--
-- Audit Triggers For InstrumentRun 
--


CREATE TRIGGER TrAI_InstrumentRun_FER AFTER INSERT ON InstrumentRun FOR EACH ROW
BEGIN
  INSERT INTO InstrumentRun_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrumentRun
  , runDate
  , createDate
  , codeInstrumentRunStatus
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idInstrumentRun
  , NEW.runDate
  , NEW.createDate
  , NEW.codeInstrumentRunStatus
  , NEW.comments
  , NEW.label
  , NEW.codeReactionType
  , NEW.creator
  , NEW.codeSealType );
END;
$$


CREATE TRIGGER TrAU_InstrumentRun_FER AFTER UPDATE ON InstrumentRun FOR EACH ROW
BEGIN
  INSERT INTO InstrumentRun_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrumentRun
  , runDate
  , createDate
  , codeInstrumentRunStatus
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idInstrumentRun
  , NEW.runDate
  , NEW.createDate
  , NEW.codeInstrumentRunStatus
  , NEW.comments
  , NEW.label
  , NEW.codeReactionType
  , NEW.creator
  , NEW.codeSealType );
END;
$$


CREATE TRIGGER TrAD_InstrumentRun_FER AFTER DELETE ON InstrumentRun FOR EACH ROW
BEGIN
  INSERT INTO InstrumentRun_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrumentRun
  , runDate
  , createDate
  , codeInstrumentRunStatus
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idInstrumentRun
  , OLD.runDate
  , OLD.createDate
  , OLD.codeInstrumentRunStatus
  , OLD.comments
  , OLD.label
  , OLD.codeReactionType
  , OLD.creator
  , OLD.codeSealType );
END;
$$


--
-- Audit Table For Instrument 
--

-- select 'Creating table Instrument'$$

-- DROP TABLE IF EXISTS `Instrument_Audit`$$

CREATE TABLE IF NOT EXISTS `Instrument_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idInstrument`  int(10)  NULL DEFAULT NULL
 ,`instrument`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Instrument 
--

INSERT INTO Instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrument
  , instrument
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idInstrument
  , instrument
  , isActive
  FROM Instrument
  WHERE NOT EXISTS(SELECT * FROM Instrument_Audit)
$$

--
-- Audit Triggers For Instrument 
--


CREATE TRIGGER TrAI_Instrument_FER AFTER INSERT ON Instrument FOR EACH ROW
BEGIN
  INSERT INTO Instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idInstrument
  , NEW.instrument
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Instrument_FER AFTER UPDATE ON Instrument FOR EACH ROW
BEGIN
  INSERT INTO Instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idInstrument
  , NEW.instrument
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Instrument_FER AFTER DELETE ON Instrument FOR EACH ROW
BEGIN
  INSERT INTO Instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idInstrument
  , OLD.instrument
  , OLD.isActive );
END;
$$


--
-- Audit Table For InternalAccountFieldsConfiguration 
--

-- select 'Creating table InternalAccountFieldsConfiguration'$$

-- DROP TABLE IF EXISTS `InternalAccountFieldsConfiguration_Audit`$$

CREATE TABLE IF NOT EXISTS `InternalAccountFieldsConfiguration_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idInternalAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`displayName`  varchar(50)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
 ,`isNumber`  char(1)  NULL DEFAULT NULL
 ,`minLength`  int(10)  NULL DEFAULT NULL
 ,`maxLength`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for InternalAccountFieldsConfiguration 
--

INSERT INTO InternalAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInternalAccountFieldsConfiguration
  , fieldName
  , include
  , sortOrder
  , displayName
  , isRequired
  , isNumber
  , minLength
  , maxLength )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idInternalAccountFieldsConfiguration
  , fieldName
  , include
  , sortOrder
  , displayName
  , isRequired
  , isNumber
  , minLength
  , maxLength
  FROM InternalAccountFieldsConfiguration
  WHERE NOT EXISTS(SELECT * FROM InternalAccountFieldsConfiguration_Audit)
$$

--
-- Audit Triggers For InternalAccountFieldsConfiguration 
--


CREATE TRIGGER TrAI_InternalAccountFieldsConfiguration_FER AFTER INSERT ON InternalAccountFieldsConfiguration FOR EACH ROW
BEGIN
  INSERT INTO InternalAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInternalAccountFieldsConfiguration
  , fieldName
  , include
  , sortOrder
  , displayName
  , isRequired
  , isNumber
  , minLength
  , maxLength )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idInternalAccountFieldsConfiguration
  , NEW.fieldName
  , NEW.include
  , NEW.sortOrder
  , NEW.displayName
  , NEW.isRequired
  , NEW.isNumber
  , NEW.minLength
  , NEW.maxLength );
END;
$$


CREATE TRIGGER TrAU_InternalAccountFieldsConfiguration_FER AFTER UPDATE ON InternalAccountFieldsConfiguration FOR EACH ROW
BEGIN
  INSERT INTO InternalAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInternalAccountFieldsConfiguration
  , fieldName
  , include
  , sortOrder
  , displayName
  , isRequired
  , isNumber
  , minLength
  , maxLength )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idInternalAccountFieldsConfiguration
  , NEW.fieldName
  , NEW.include
  , NEW.sortOrder
  , NEW.displayName
  , NEW.isRequired
  , NEW.isNumber
  , NEW.minLength
  , NEW.maxLength );
END;
$$


CREATE TRIGGER TrAD_InternalAccountFieldsConfiguration_FER AFTER DELETE ON InternalAccountFieldsConfiguration FOR EACH ROW
BEGIN
  INSERT INTO InternalAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInternalAccountFieldsConfiguration
  , fieldName
  , include
  , sortOrder
  , displayName
  , isRequired
  , isNumber
  , minLength
  , maxLength )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idInternalAccountFieldsConfiguration
  , OLD.fieldName
  , OLD.include
  , OLD.sortOrder
  , OLD.displayName
  , OLD.isRequired
  , OLD.isNumber
  , OLD.minLength
  , OLD.maxLength );
END;
$$


--
-- Audit Table For Invoice 
--

-- select 'Creating table Invoice'$$

-- DROP TABLE IF EXISTS `Invoice_Audit`$$

CREATE TABLE IF NOT EXISTS `Invoice_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`invoiceNumber`  varchar(50)  NULL DEFAULT NULL
 ,`lastEmailDate`  datetime  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Invoice 
--

INSERT INTO Invoice_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate
  FROM Invoice
  WHERE NOT EXISTS(SELECT * FROM Invoice_Audit)
$$

--
-- Audit Triggers For Invoice 
--


CREATE TRIGGER TrAI_Invoice_FER AFTER INSERT ON Invoice FOR EACH ROW
BEGIN
  INSERT INTO Invoice_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idInvoice
  , NEW.idCoreFacility
  , NEW.idBillingPeriod
  , NEW.idBillingAccount
  , NEW.invoiceNumber
  , NEW.lastEmailDate );
END;
$$


CREATE TRIGGER TrAU_Invoice_FER AFTER UPDATE ON Invoice FOR EACH ROW
BEGIN
  INSERT INTO Invoice_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idInvoice
  , NEW.idCoreFacility
  , NEW.idBillingPeriod
  , NEW.idBillingAccount
  , NEW.invoiceNumber
  , NEW.lastEmailDate );
END;
$$


CREATE TRIGGER TrAD_Invoice_FER AFTER DELETE ON Invoice FOR EACH ROW
BEGIN
  INSERT INTO Invoice_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idInvoice
  , OLD.idCoreFacility
  , OLD.idBillingPeriod
  , OLD.idBillingAccount
  , OLD.invoiceNumber
  , OLD.lastEmailDate );
END;
$$


--
-- Audit Table For IsolationPrepType 
--

-- select 'Creating table IsolationPrepType'$$

-- DROP TABLE IF EXISTS `IsolationPrepType_Audit`$$

CREATE TABLE IF NOT EXISTS `IsolationPrepType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeIsolationPrepType`  varchar(15)  NULL DEFAULT NULL
 ,`isolationPrepType`  varchar(100)  NULL DEFAULT NULL
 ,`type`  varchar(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(50)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for IsolationPrepType 
--

INSERT INTO IsolationPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory
  FROM IsolationPrepType
  WHERE NOT EXISTS(SELECT * FROM IsolationPrepType_Audit)
$$

--
-- Audit Triggers For IsolationPrepType 
--


CREATE TRIGGER TrAI_IsolationPrepType_FER AFTER INSERT ON IsolationPrepType FOR EACH ROW
BEGIN
  INSERT INTO IsolationPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeIsolationPrepType
  , NEW.isolationPrepType
  , NEW.type
  , NEW.isActive
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_IsolationPrepType_FER AFTER UPDATE ON IsolationPrepType FOR EACH ROW
BEGIN
  INSERT INTO IsolationPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeIsolationPrepType
  , NEW.isolationPrepType
  , NEW.type
  , NEW.isActive
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_IsolationPrepType_FER AFTER DELETE ON IsolationPrepType FOR EACH ROW
BEGIN
  INSERT INTO IsolationPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeIsolationPrepType
  , OLD.isolationPrepType
  , OLD.type
  , OLD.isActive
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For LabCollaborator 
--

-- select 'Creating table LabCollaborator'$$

-- DROP TABLE IF EXISTS `LabCollaborator_Audit`$$

CREATE TABLE IF NOT EXISTS `LabCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for LabCollaborator 
--

INSERT INTO LabCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLab
  , idAppUser
  , sendUploadAlert
  FROM LabCollaborator
  WHERE NOT EXISTS(SELECT * FROM LabCollaborator_Audit)
$$

--
-- Audit Triggers For LabCollaborator 
--


CREATE TRIGGER TrAI_LabCollaborator_FER AFTER INSERT ON LabCollaborator FOR EACH ROW
BEGIN
  INSERT INTO LabCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAU_LabCollaborator_FER AFTER UPDATE ON LabCollaborator FOR EACH ROW
BEGIN
  INSERT INTO LabCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAD_LabCollaborator_FER AFTER DELETE ON LabCollaborator FOR EACH ROW
BEGIN
  INSERT INTO LabCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For LabeledSample 
--

-- select 'Creating table LabeledSample'$$

-- DROP TABLE IF EXISTS `LabeledSample_Audit`$$

CREATE TABLE IF NOT EXISTS `LabeledSample_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLabeledSample`  int(10)  NULL DEFAULT NULL
 ,`labelingYield`  decimal(8,2)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idLabel`  int(10)  NULL DEFAULT NULL
 ,`idLabelingProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeLabelingReactionSize`  varchar(20)  NULL DEFAULT NULL
 ,`numberOfReactions`  int(10)  NULL DEFAULT NULL
 ,`labelingDate`  datetime  NULL DEFAULT NULL
 ,`labelingFailed`  char(1)  NULL DEFAULT NULL
 ,`labelingBypassed`  char(1)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for LabeledSample 
--

INSERT INTO LabeledSample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabeledSample
  , labelingYield
  , idSample
  , idLabel
  , idLabelingProtocol
  , codeLabelingReactionSize
  , numberOfReactions
  , labelingDate
  , labelingFailed
  , labelingBypassed
  , idRequest )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLabeledSample
  , labelingYield
  , idSample
  , idLabel
  , idLabelingProtocol
  , codeLabelingReactionSize
  , numberOfReactions
  , labelingDate
  , labelingFailed
  , labelingBypassed
  , idRequest
  FROM LabeledSample
  WHERE NOT EXISTS(SELECT * FROM LabeledSample_Audit)
$$

--
-- Audit Triggers For LabeledSample 
--


CREATE TRIGGER TrAI_LabeledSample_FER AFTER INSERT ON LabeledSample FOR EACH ROW
BEGIN
  INSERT INTO LabeledSample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabeledSample
  , labelingYield
  , idSample
  , idLabel
  , idLabelingProtocol
  , codeLabelingReactionSize
  , numberOfReactions
  , labelingDate
  , labelingFailed
  , labelingBypassed
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLabeledSample
  , NEW.labelingYield
  , NEW.idSample
  , NEW.idLabel
  , NEW.idLabelingProtocol
  , NEW.codeLabelingReactionSize
  , NEW.numberOfReactions
  , NEW.labelingDate
  , NEW.labelingFailed
  , NEW.labelingBypassed
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAU_LabeledSample_FER AFTER UPDATE ON LabeledSample FOR EACH ROW
BEGIN
  INSERT INTO LabeledSample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabeledSample
  , labelingYield
  , idSample
  , idLabel
  , idLabelingProtocol
  , codeLabelingReactionSize
  , numberOfReactions
  , labelingDate
  , labelingFailed
  , labelingBypassed
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLabeledSample
  , NEW.labelingYield
  , NEW.idSample
  , NEW.idLabel
  , NEW.idLabelingProtocol
  , NEW.codeLabelingReactionSize
  , NEW.numberOfReactions
  , NEW.labelingDate
  , NEW.labelingFailed
  , NEW.labelingBypassed
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAD_LabeledSample_FER AFTER DELETE ON LabeledSample FOR EACH ROW
BEGIN
  INSERT INTO LabeledSample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabeledSample
  , labelingYield
  , idSample
  , idLabel
  , idLabelingProtocol
  , codeLabelingReactionSize
  , numberOfReactions
  , labelingDate
  , labelingFailed
  , labelingBypassed
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLabeledSample
  , OLD.labelingYield
  , OLD.idSample
  , OLD.idLabel
  , OLD.idLabelingProtocol
  , OLD.codeLabelingReactionSize
  , OLD.numberOfReactions
  , OLD.labelingDate
  , OLD.labelingFailed
  , OLD.labelingBypassed
  , OLD.idRequest );
END;
$$


--
-- Audit Table For LabelingProtocol 
--

-- select 'Creating table LabelingProtocol'$$

-- DROP TABLE IF EXISTS `LabelingProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `LabelingProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLabelingProtocol`  int(10)  NULL DEFAULT NULL
 ,`labelingProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for LabelingProtocol 
--

INSERT INTO LabelingProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive
  FROM LabelingProtocol
  WHERE NOT EXISTS(SELECT * FROM LabelingProtocol_Audit)
$$

--
-- Audit Triggers For LabelingProtocol 
--


CREATE TRIGGER TrAI_LabelingProtocol_FER AFTER INSERT ON LabelingProtocol FOR EACH ROW
BEGIN
  INSERT INTO LabelingProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLabelingProtocol
  , NEW.labelingProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_LabelingProtocol_FER AFTER UPDATE ON LabelingProtocol FOR EACH ROW
BEGIN
  INSERT INTO LabelingProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLabelingProtocol
  , NEW.labelingProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_LabelingProtocol_FER AFTER DELETE ON LabelingProtocol FOR EACH ROW
BEGIN
  INSERT INTO LabelingProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLabelingProtocol
  , OLD.labelingProtocol
  , OLD.codeRequestCategory
  , OLD.description
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For LabelingReactionSize 
--

-- select 'Creating table LabelingReactionSize'$$

-- DROP TABLE IF EXISTS `LabelingReactionSize_Audit`$$

CREATE TABLE IF NOT EXISTS `LabelingReactionSize_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeLabelingReactionSize`  varchar(20)  NULL DEFAULT NULL
 ,`labelingReactionSize`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for LabelingReactionSize 
--

INSERT INTO LabelingReactionSize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder
  FROM LabelingReactionSize
  WHERE NOT EXISTS(SELECT * FROM LabelingReactionSize_Audit)
$$

--
-- Audit Triggers For LabelingReactionSize 
--


CREATE TRIGGER TrAI_LabelingReactionSize_FER AFTER INSERT ON LabelingReactionSize FOR EACH ROW
BEGIN
  INSERT INTO LabelingReactionSize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeLabelingReactionSize
  , NEW.labelingReactionSize
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_LabelingReactionSize_FER AFTER UPDATE ON LabelingReactionSize FOR EACH ROW
BEGIN
  INSERT INTO LabelingReactionSize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeLabelingReactionSize
  , NEW.labelingReactionSize
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_LabelingReactionSize_FER AFTER DELETE ON LabelingReactionSize FOR EACH ROW
BEGIN
  INSERT INTO LabelingReactionSize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeLabelingReactionSize
  , OLD.labelingReactionSize
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For Label 
--

-- select 'Creating table Label'$$

-- DROP TABLE IF EXISTS `Label_Audit`$$

CREATE TABLE IF NOT EXISTS `Label_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLabel`  int(10)  NULL DEFAULT NULL
 ,`label`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Label 
--

INSERT INTO Label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabel
  , label
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLabel
  , label
  , isActive
  FROM Label
  WHERE NOT EXISTS(SELECT * FROM Label_Audit)
$$

--
-- Audit Triggers For Label 
--


CREATE TRIGGER TrAI_Label_FER AFTER INSERT ON Label FOR EACH ROW
BEGIN
  INSERT INTO Label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabel
  , label
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLabel
  , NEW.label
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Label_FER AFTER UPDATE ON Label FOR EACH ROW
BEGIN
  INSERT INTO Label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabel
  , label
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLabel
  , NEW.label
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Label_FER AFTER DELETE ON Label FOR EACH ROW
BEGIN
  INSERT INTO Label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLabel
  , label
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLabel
  , OLD.label
  , OLD.isActive );
END;
$$


--
-- Audit Table For LabManager 
--

-- select 'Creating table LabManager'$$

-- DROP TABLE IF EXISTS `LabManager_Audit`$$

CREATE TABLE IF NOT EXISTS `LabManager_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for LabManager 
--

INSERT INTO LabManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLab
  , idAppUser
  , sendUploadAlert
  FROM LabManager
  WHERE NOT EXISTS(SELECT * FROM LabManager_Audit)
$$

--
-- Audit Triggers For LabManager 
--


CREATE TRIGGER TrAI_LabManager_FER AFTER INSERT ON LabManager FOR EACH ROW
BEGIN
  INSERT INTO LabManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAU_LabManager_FER AFTER UPDATE ON LabManager FOR EACH ROW
BEGIN
  INSERT INTO LabManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAD_LabManager_FER AFTER DELETE ON LabManager FOR EACH ROW
BEGIN
  INSERT INTO LabManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For LabUser 
--

-- select 'Creating table LabUser'$$

-- DROP TABLE IF EXISTS `LabUser_Audit`$$

CREATE TABLE IF NOT EXISTS `LabUser_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for LabUser 
--

INSERT INTO LabUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert
  FROM LabUser
  WHERE NOT EXISTS(SELECT * FROM LabUser_Audit)
$$

--
-- Audit Triggers For LabUser 
--


CREATE TRIGGER TrAI_LabUser_FER AFTER INSERT ON LabUser FOR EACH ROW
BEGIN
  INSERT INTO LabUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sortOrder
  , NEW.isActive
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAU_LabUser_FER AFTER UPDATE ON LabUser FOR EACH ROW
BEGIN
  INSERT INTO LabUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sortOrder
  , NEW.isActive
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAD_LabUser_FER AFTER DELETE ON LabUser FOR EACH ROW
BEGIN
  INSERT INTO LabUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sortOrder
  , OLD.isActive
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For Lab 
--

-- select 'Creating table Lab'$$

-- DROP TABLE IF EXISTS `Lab_Audit`$$

CREATE TABLE IF NOT EXISTS `Lab_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`department`  varchar(200)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`contactName`  varchar(200)  NULL DEFAULT NULL
 ,`contactAddress`  varchar(200)  NULL DEFAULT NULL
 ,`contactCodeState`  varchar(10)  NULL DEFAULT NULL
 ,`contactZip`  varchar(50)  NULL DEFAULT NULL
 ,`contactCity`  varchar(200)  NULL DEFAULT NULL
 ,`contactPhone`  varchar(50)  NULL DEFAULT NULL
 ,`contactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`isCCSGMember`  char(1)  NULL DEFAULT NULL
 ,`firstName`  varchar(200)  NULL DEFAULT NULL
 ,`lastName`  varchar(200)  NULL DEFAULT NULL
 ,`isExternalPricing`  varchar(1)  NULL DEFAULT NULL
 ,`isExternalPricingCommercial`  char(1)  NULL DEFAULT NULL
 ,`isActive`  varchar(1)  NULL DEFAULT NULL
 ,`excludeUsage`  char(1)  NULL DEFAULT NULL
 ,`billingContactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`version`  bigint(20)  NULL DEFAULT NULL
 ,`contactAddress2`  varchar(200)  NULL DEFAULT NULL
 ,`contactCountry`  varchar(200)  NULL DEFAULT NULL
 ,`billingContactPhone`  varchar(50)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Lab 
--

INSERT INTO Lab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , name
  , department
  , notes
  , contactName
  , contactAddress
  , contactCodeState
  , contactZip
  , contactCity
  , contactPhone
  , contactEmail
  , isCCSGMember
  , firstName
  , lastName
  , isExternalPricing
  , isExternalPricingCommercial
  , isActive
  , excludeUsage
  , billingContactEmail
  , version
  , contactAddress2
  , contactCountry
  , billingContactPhone )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLab
  , name
  , department
  , notes
  , contactName
  , contactAddress
  , contactCodeState
  , contactZip
  , contactCity
  , contactPhone
  , contactEmail
  , isCCSGMember
  , firstName
  , lastName
  , isExternalPricing
  , isExternalPricingCommercial
  , isActive
  , excludeUsage
  , billingContactEmail
  , version
  , contactAddress2
  , contactCountry
  , billingContactPhone
  FROM Lab
  WHERE NOT EXISTS(SELECT * FROM Lab_Audit)
$$

--
-- Audit Triggers For Lab 
--


CREATE TRIGGER TrAI_Lab_FER AFTER INSERT ON Lab FOR EACH ROW
BEGIN
  INSERT INTO Lab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , name
  , department
  , notes
  , contactName
  , contactAddress
  , contactCodeState
  , contactZip
  , contactCity
  , contactPhone
  , contactEmail
  , isCCSGMember
  , firstName
  , lastName
  , isExternalPricing
  , isExternalPricingCommercial
  , isActive
  , excludeUsage
  , billingContactEmail
  , version
  , contactAddress2
  , contactCountry
  , billingContactPhone )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.name
  , NEW.department
  , NEW.notes
  , NEW.contactName
  , NEW.contactAddress
  , NEW.contactCodeState
  , NEW.contactZip
  , NEW.contactCity
  , NEW.contactPhone
  , NEW.contactEmail
  , NEW.isCCSGMember
  , NEW.firstName
  , NEW.lastName
  , NEW.isExternalPricing
  , NEW.isExternalPricingCommercial
  , NEW.isActive
  , NEW.excludeUsage
  , NEW.billingContactEmail
  , NEW.version
  , NEW.contactAddress2
  , NEW.contactCountry
  , NEW.billingContactPhone );
END;
$$


CREATE TRIGGER TrAU_Lab_FER AFTER UPDATE ON Lab FOR EACH ROW
BEGIN
  INSERT INTO Lab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , name
  , department
  , notes
  , contactName
  , contactAddress
  , contactCodeState
  , contactZip
  , contactCity
  , contactPhone
  , contactEmail
  , isCCSGMember
  , firstName
  , lastName
  , isExternalPricing
  , isExternalPricingCommercial
  , isActive
  , excludeUsage
  , billingContactEmail
  , version
  , contactAddress2
  , contactCountry
  , billingContactPhone )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLab
  , NEW.name
  , NEW.department
  , NEW.notes
  , NEW.contactName
  , NEW.contactAddress
  , NEW.contactCodeState
  , NEW.contactZip
  , NEW.contactCity
  , NEW.contactPhone
  , NEW.contactEmail
  , NEW.isCCSGMember
  , NEW.firstName
  , NEW.lastName
  , NEW.isExternalPricing
  , NEW.isExternalPricingCommercial
  , NEW.isActive
  , NEW.excludeUsage
  , NEW.billingContactEmail
  , NEW.version
  , NEW.contactAddress2
  , NEW.contactCountry
  , NEW.billingContactPhone );
END;
$$


CREATE TRIGGER TrAD_Lab_FER AFTER DELETE ON Lab FOR EACH ROW
BEGIN
  INSERT INTO Lab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLab
  , name
  , department
  , notes
  , contactName
  , contactAddress
  , contactCodeState
  , contactZip
  , contactCity
  , contactPhone
  , contactEmail
  , isCCSGMember
  , firstName
  , lastName
  , isExternalPricing
  , isExternalPricingCommercial
  , isActive
  , excludeUsage
  , billingContactEmail
  , version
  , contactAddress2
  , contactCountry
  , billingContactPhone )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLab
  , OLD.name
  , OLD.department
  , OLD.notes
  , OLD.contactName
  , OLD.contactAddress
  , OLD.contactCodeState
  , OLD.contactZip
  , OLD.contactCity
  , OLD.contactPhone
  , OLD.contactEmail
  , OLD.isCCSGMember
  , OLD.firstName
  , OLD.lastName
  , OLD.isExternalPricing
  , OLD.isExternalPricingCommercial
  , OLD.isActive
  , OLD.excludeUsage
  , OLD.billingContactEmail
  , OLD.version
  , OLD.contactAddress2
  , OLD.contactCountry
  , OLD.billingContactPhone );
END;
$$


--
-- Audit Table For LibraryPrepQCProtocol 
--

-- select 'Creating table LibraryPrepQCProtocol'$$

-- DROP TABLE IF EXISTS `LibraryPrepQCProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `LibraryPrepQCProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idLibPrepQCProtocol`  int(10)  NULL DEFAULT NULL
 ,`protocolDisplay`  varchar(50)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for LibraryPrepQCProtocol 
--

INSERT INTO LibraryPrepQCProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLibPrepQCProtocol
  , protocolDisplay
  , codeRequestCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idLibPrepQCProtocol
  , protocolDisplay
  , codeRequestCategory
  FROM LibraryPrepQCProtocol
  WHERE NOT EXISTS(SELECT * FROM LibraryPrepQCProtocol_Audit)
$$

--
-- Audit Triggers For LibraryPrepQCProtocol 
--


CREATE TRIGGER TrAI_LibraryPrepQCProtocol_FER AFTER INSERT ON LibraryPrepQCProtocol FOR EACH ROW
BEGIN
  INSERT INTO LibraryPrepQCProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLibPrepQCProtocol
  , protocolDisplay
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idLibPrepQCProtocol
  , NEW.protocolDisplay
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_LibraryPrepQCProtocol_FER AFTER UPDATE ON LibraryPrepQCProtocol FOR EACH ROW
BEGIN
  INSERT INTO LibraryPrepQCProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLibPrepQCProtocol
  , protocolDisplay
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idLibPrepQCProtocol
  , NEW.protocolDisplay
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_LibraryPrepQCProtocol_FER AFTER DELETE ON LibraryPrepQCProtocol FOR EACH ROW
BEGIN
  INSERT INTO LibraryPrepQCProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idLibPrepQCProtocol
  , protocolDisplay
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idLibPrepQCProtocol
  , OLD.protocolDisplay
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For MasterBillingItem 
--

-- select 'Creating table MasterBillingItem'$$

-- DROP TABLE IF EXISTS `MasterBillingItem_Audit`$$

CREATE TABLE IF NOT EXISTS `MasterBillingItem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idMasterBillingItem`  int(10)  NULL DEFAULT NULL
 ,`idBillingTemplate`  int(10)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`category`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`totalPrice`  decimal(9,2)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for MasterBillingItem 
--

INSERT INTO MasterBillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idMasterBillingItem
  , idBillingTemplate
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , totalPrice
  , idBillingPeriod
  , idPrice
  , idPriceCategory
  , idCoreFacility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idMasterBillingItem
  , idBillingTemplate
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , totalPrice
  , idBillingPeriod
  , idPrice
  , idPriceCategory
  , idCoreFacility
  FROM MasterBillingItem
  WHERE NOT EXISTS(SELECT * FROM MasterBillingItem_Audit)
$$

--
-- Audit Triggers For MasterBillingItem 
--


CREATE TRIGGER TrAI_MasterBillingItem_FER AFTER INSERT ON MasterBillingItem FOR EACH ROW
BEGIN
  INSERT INTO MasterBillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idMasterBillingItem
  , idBillingTemplate
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , totalPrice
  , idBillingPeriod
  , idPrice
  , idPriceCategory
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idMasterBillingItem
  , NEW.idBillingTemplate
  , NEW.codeBillingChargeKind
  , NEW.category
  , NEW.description
  , NEW.qty
  , NEW.unitPrice
  , NEW.totalPrice
  , NEW.idBillingPeriod
  , NEW.idPrice
  , NEW.idPriceCategory
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_MasterBillingItem_FER AFTER UPDATE ON MasterBillingItem FOR EACH ROW
BEGIN
  INSERT INTO MasterBillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idMasterBillingItem
  , idBillingTemplate
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , totalPrice
  , idBillingPeriod
  , idPrice
  , idPriceCategory
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idMasterBillingItem
  , NEW.idBillingTemplate
  , NEW.codeBillingChargeKind
  , NEW.category
  , NEW.description
  , NEW.qty
  , NEW.unitPrice
  , NEW.totalPrice
  , NEW.idBillingPeriod
  , NEW.idPrice
  , NEW.idPriceCategory
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_MasterBillingItem_FER AFTER DELETE ON MasterBillingItem FOR EACH ROW
BEGIN
  INSERT INTO MasterBillingItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idMasterBillingItem
  , idBillingTemplate
  , codeBillingChargeKind
  , category
  , description
  , qty
  , unitPrice
  , totalPrice
  , idBillingPeriod
  , idPrice
  , idPriceCategory
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idMasterBillingItem
  , OLD.idBillingTemplate
  , OLD.codeBillingChargeKind
  , OLD.category
  , OLD.description
  , OLD.qty
  , OLD.unitPrice
  , OLD.totalPrice
  , OLD.idBillingPeriod
  , OLD.idPrice
  , OLD.idPriceCategory
  , OLD.idCoreFacility );
END;
$$


--
-- Audit Table For MetrixObject 
--

-- select 'Creating table MetrixObject'$$

-- DROP TABLE IF EXISTS `MetrixObject_Audit`$$

CREATE TABLE IF NOT EXISTS `MetrixObject_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`id`  int(10)  NULL DEFAULT NULL
 ,`run_id`  varchar(512)  NULL DEFAULT NULL
 ,`object_value`  varbinary(8000)  NULL DEFAULT NULL
 ,`state`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for MetrixObject 
--

INSERT INTO MetrixObject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , id
  , run_id
  , object_value
  , state )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , id
  , run_id
  , object_value
  , state
  FROM MetrixObject
  WHERE NOT EXISTS(SELECT * FROM MetrixObject_Audit)
$$

--
-- Audit Triggers For MetrixObject 
--


CREATE TRIGGER TrAI_MetrixObject_FER AFTER INSERT ON MetrixObject FOR EACH ROW
BEGIN
  INSERT INTO MetrixObject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.id
  , NEW.run_id
  , NEW.object_value
  , NEW.state );
END;
$$


CREATE TRIGGER TrAU_MetrixObject_FER AFTER UPDATE ON MetrixObject FOR EACH ROW
BEGIN
  INSERT INTO MetrixObject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.id
  , NEW.run_id
  , NEW.object_value
  , NEW.state );
END;
$$


CREATE TRIGGER TrAD_MetrixObject_FER AFTER DELETE ON MetrixObject FOR EACH ROW
BEGIN
  INSERT INTO MetrixObject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.id
  , OLD.run_id
  , OLD.object_value
  , OLD.state );
END;
$$


--
-- Audit Table For NewsItem 
--

-- select 'Creating table NewsItem'$$

-- DROP TABLE IF EXISTS `NewsItem_Audit`$$

CREATE TABLE IF NOT EXISTS `NewsItem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idNewsItem`  int(10)  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`title`  varchar(200)  NULL DEFAULT NULL
 ,`message`  varchar(4000)  NULL DEFAULT NULL
 ,`date`  datetime  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for NewsItem 
--

INSERT INTO NewsItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date
  FROM NewsItem
  WHERE NOT EXISTS(SELECT * FROM NewsItem_Audit)
$$

--
-- Audit Triggers For NewsItem 
--


CREATE TRIGGER TrAI_NewsItem_FER AFTER INSERT ON NewsItem FOR EACH ROW
BEGIN
  INSERT INTO NewsItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idNewsItem
  , NEW.idSubmitter
  , NEW.idCoreFacility
  , NEW.title
  , NEW.message
  , NEW.date );
END;
$$


CREATE TRIGGER TrAU_NewsItem_FER AFTER UPDATE ON NewsItem FOR EACH ROW
BEGIN
  INSERT INTO NewsItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idNewsItem
  , NEW.idSubmitter
  , NEW.idCoreFacility
  , NEW.title
  , NEW.message
  , NEW.date );
END;
$$


CREATE TRIGGER TrAD_NewsItem_FER AFTER DELETE ON NewsItem FOR EACH ROW
BEGIN
  INSERT INTO NewsItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idNewsItem
  , OLD.idSubmitter
  , OLD.idCoreFacility
  , OLD.title
  , OLD.message
  , OLD.date );
END;
$$


--
-- Audit Table For Notification 
--

-- select 'Creating table Notification'$$

-- DROP TABLE IF EXISTS `Notification_Audit`$$

CREATE TABLE IF NOT EXISTS `Notification_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idNotification`  int(10)  NULL DEFAULT NULL
 ,`idUserTarget`  int(10)  NULL DEFAULT NULL
 ,`idLabTarget`  int(10)  NULL DEFAULT NULL
 ,`sourceType`  varchar(20)  NULL DEFAULT NULL
 ,`message`  varchar(250)  NULL DEFAULT NULL
 ,`date`  datetime  NULL DEFAULT NULL
 ,`expID`  varchar(25)  NULL DEFAULT NULL
 ,`type`  varchar(25)  NULL DEFAULT NULL
 ,`fullNameUser`  varchar(100)  NULL DEFAULT NULL
 ,`imageSource`  varchar(50)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Notification 
--

INSERT INTO Notification_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNotification
  , idUserTarget
  , idLabTarget
  , sourceType
  , message
  , date
  , expID
  , type
  , fullNameUser
  , imageSource
  , idCoreFacility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idNotification
  , idUserTarget
  , idLabTarget
  , sourceType
  , message
  , date
  , expID
  , type
  , fullNameUser
  , imageSource
  , idCoreFacility
  FROM Notification
  WHERE NOT EXISTS(SELECT * FROM Notification_Audit)
$$

--
-- Audit Triggers For Notification 
--


CREATE TRIGGER TrAI_Notification_FER AFTER INSERT ON Notification FOR EACH ROW
BEGIN
  INSERT INTO Notification_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNotification
  , idUserTarget
  , idLabTarget
  , sourceType
  , message
  , date
  , expID
  , type
  , fullNameUser
  , imageSource
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idNotification
  , NEW.idUserTarget
  , NEW.idLabTarget
  , NEW.sourceType
  , NEW.message
  , NEW.date
  , NEW.expID
  , NEW.type
  , NEW.fullNameUser
  , NEW.imageSource
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_Notification_FER AFTER UPDATE ON Notification FOR EACH ROW
BEGIN
  INSERT INTO Notification_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNotification
  , idUserTarget
  , idLabTarget
  , sourceType
  , message
  , date
  , expID
  , type
  , fullNameUser
  , imageSource
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idNotification
  , NEW.idUserTarget
  , NEW.idLabTarget
  , NEW.sourceType
  , NEW.message
  , NEW.date
  , NEW.expID
  , NEW.type
  , NEW.fullNameUser
  , NEW.imageSource
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_Notification_FER AFTER DELETE ON Notification FOR EACH ROW
BEGIN
  INSERT INTO Notification_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNotification
  , idUserTarget
  , idLabTarget
  , sourceType
  , message
  , date
  , expID
  , type
  , fullNameUser
  , imageSource
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idNotification
  , OLD.idUserTarget
  , OLD.idLabTarget
  , OLD.sourceType
  , OLD.message
  , OLD.date
  , OLD.expID
  , OLD.type
  , OLD.fullNameUser
  , OLD.imageSource
  , OLD.idCoreFacility );
END;
$$


--
-- Audit Table For NucleotideType 
--

-- select 'Creating table NucleotideType'$$

-- DROP TABLE IF EXISTS `NucleotideType_Audit`$$

CREATE TABLE IF NOT EXISTS `NucleotideType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for NucleotideType 
--

INSERT INTO NucleotideType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeNucleotideType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeNucleotideType
  FROM NucleotideType
  WHERE NOT EXISTS(SELECT * FROM NucleotideType_Audit)
$$

--
-- Audit Triggers For NucleotideType 
--


CREATE TRIGGER TrAI_NucleotideType_FER AFTER INSERT ON NucleotideType FOR EACH ROW
BEGIN
  INSERT INTO NucleotideType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeNucleotideType );
END;
$$


CREATE TRIGGER TrAU_NucleotideType_FER AFTER UPDATE ON NucleotideType FOR EACH ROW
BEGIN
  INSERT INTO NucleotideType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeNucleotideType );
END;
$$


CREATE TRIGGER TrAD_NucleotideType_FER AFTER DELETE ON NucleotideType FOR EACH ROW
BEGIN
  INSERT INTO NucleotideType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeNucleotideType );
END;
$$


--
-- Audit Table For NumberSequencingCyclesAllowed 
--

-- select 'Creating table NumberSequencingCyclesAllowed'$$

-- DROP TABLE IF EXISTS `NumberSequencingCyclesAllowed_Audit`$$

CREATE TABLE IF NOT EXISTS `NumberSequencingCyclesAllowed_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`protocolDescription`  longtext  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for NumberSequencingCyclesAllowed 
--

INSERT INTO NumberSequencingCyclesAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCyclesAllowed
  , idNumberSequencingCycles
  , codeRequestCategory
  , idSeqRunType
  , name
  , isCustom
  , sortOrder
  , isActive
  , protocolDescription )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idNumberSequencingCyclesAllowed
  , idNumberSequencingCycles
  , codeRequestCategory
  , idSeqRunType
  , name
  , isCustom
  , sortOrder
  , isActive
  , protocolDescription
  FROM NumberSequencingCyclesAllowed
  WHERE NOT EXISTS(SELECT * FROM NumberSequencingCyclesAllowed_Audit)
$$

--
-- Audit Triggers For NumberSequencingCyclesAllowed 
--


CREATE TRIGGER TrAI_NumberSequencingCyclesAllowed_FER AFTER INSERT ON NumberSequencingCyclesAllowed FOR EACH ROW
BEGIN
  INSERT INTO NumberSequencingCyclesAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCyclesAllowed
  , idNumberSequencingCycles
  , codeRequestCategory
  , idSeqRunType
  , name
  , isCustom
  , sortOrder
  , isActive
  , protocolDescription )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idNumberSequencingCyclesAllowed
  , NEW.idNumberSequencingCycles
  , NEW.codeRequestCategory
  , NEW.idSeqRunType
  , NEW.name
  , NEW.isCustom
  , NEW.sortOrder
  , NEW.isActive
  , NEW.protocolDescription );
END;
$$


CREATE TRIGGER TrAU_NumberSequencingCyclesAllowed_FER AFTER UPDATE ON NumberSequencingCyclesAllowed FOR EACH ROW
BEGIN
  INSERT INTO NumberSequencingCyclesAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCyclesAllowed
  , idNumberSequencingCycles
  , codeRequestCategory
  , idSeqRunType
  , name
  , isCustom
  , sortOrder
  , isActive
  , protocolDescription )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idNumberSequencingCyclesAllowed
  , NEW.idNumberSequencingCycles
  , NEW.codeRequestCategory
  , NEW.idSeqRunType
  , NEW.name
  , NEW.isCustom
  , NEW.sortOrder
  , NEW.isActive
  , NEW.protocolDescription );
END;
$$


CREATE TRIGGER TrAD_NumberSequencingCyclesAllowed_FER AFTER DELETE ON NumberSequencingCyclesAllowed FOR EACH ROW
BEGIN
  INSERT INTO NumberSequencingCyclesAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCyclesAllowed
  , idNumberSequencingCycles
  , codeRequestCategory
  , idSeqRunType
  , name
  , isCustom
  , sortOrder
  , isActive
  , protocolDescription )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idNumberSequencingCyclesAllowed
  , OLD.idNumberSequencingCycles
  , OLD.codeRequestCategory
  , OLD.idSeqRunType
  , OLD.name
  , OLD.isCustom
  , OLD.sortOrder
  , OLD.isActive
  , OLD.protocolDescription );
END;
$$


--
-- Audit Table For NumberSequencingCycles 
--

-- select 'Creating table NumberSequencingCycles'$$

-- DROP TABLE IF EXISTS `NumberSequencingCycles_Audit`$$

CREATE TABLE IF NOT EXISTS `NumberSequencingCycles_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`numberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for NumberSequencingCycles 
--

INSERT INTO NumberSequencingCycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes
  FROM NumberSequencingCycles
  WHERE NOT EXISTS(SELECT * FROM NumberSequencingCycles_Audit)
$$

--
-- Audit Triggers For NumberSequencingCycles 
--


CREATE TRIGGER TrAI_NumberSequencingCycles_FER AFTER INSERT ON NumberSequencingCycles FOR EACH ROW
BEGIN
  INSERT INTO NumberSequencingCycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idNumberSequencingCycles
  , NEW.numberSequencingCycles
  , NEW.isActive
  , NEW.sortOrder
  , NEW.notes );
END;
$$


CREATE TRIGGER TrAU_NumberSequencingCycles_FER AFTER UPDATE ON NumberSequencingCycles FOR EACH ROW
BEGIN
  INSERT INTO NumberSequencingCycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idNumberSequencingCycles
  , NEW.numberSequencingCycles
  , NEW.isActive
  , NEW.sortOrder
  , NEW.notes );
END;
$$


CREATE TRIGGER TrAD_NumberSequencingCycles_FER AFTER DELETE ON NumberSequencingCycles FOR EACH ROW
BEGIN
  INSERT INTO NumberSequencingCycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idNumberSequencingCycles
  , OLD.numberSequencingCycles
  , OLD.isActive
  , OLD.sortOrder
  , OLD.notes );
END;
$$


--
-- Audit Table For OligoBarcodeSchemeAllowed 
--

-- select 'Creating table OligoBarcodeSchemeAllowed'$$

-- DROP TABLE IF EXISTS `OligoBarcodeSchemeAllowed_Audit`$$

CREATE TABLE IF NOT EXISTS `OligoBarcodeSchemeAllowed_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idOligoBarcodeSchemeAllowed`  int(10)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`isIndexGroupB`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for OligoBarcodeSchemeAllowed 
--

INSERT INTO OligoBarcodeSchemeAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB
  FROM OligoBarcodeSchemeAllowed
  WHERE NOT EXISTS(SELECT * FROM OligoBarcodeSchemeAllowed_Audit)
$$

--
-- Audit Triggers For OligoBarcodeSchemeAllowed 
--


CREATE TRIGGER TrAI_OligoBarcodeSchemeAllowed_FER AFTER INSERT ON OligoBarcodeSchemeAllowed FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcodeSchemeAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idOligoBarcodeSchemeAllowed
  , NEW.idOligoBarcodeScheme
  , NEW.idSeqLibProtocol
  , NEW.isIndexGroupB );
END;
$$


CREATE TRIGGER TrAU_OligoBarcodeSchemeAllowed_FER AFTER UPDATE ON OligoBarcodeSchemeAllowed FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcodeSchemeAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idOligoBarcodeSchemeAllowed
  , NEW.idOligoBarcodeScheme
  , NEW.idSeqLibProtocol
  , NEW.isIndexGroupB );
END;
$$


CREATE TRIGGER TrAD_OligoBarcodeSchemeAllowed_FER AFTER DELETE ON OligoBarcodeSchemeAllowed FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcodeSchemeAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idOligoBarcodeSchemeAllowed
  , OLD.idOligoBarcodeScheme
  , OLD.idSeqLibProtocol
  , OLD.isIndexGroupB );
END;
$$


--
-- Audit Table For OligoBarcodeScheme 
--

-- select 'Creating table OligoBarcodeScheme'$$

-- DROP TABLE IF EXISTS `OligoBarcodeScheme_Audit`$$

CREATE TABLE IF NOT EXISTS `OligoBarcodeScheme_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`oligoBarcodeScheme`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for OligoBarcodeScheme 
--

INSERT INTO OligoBarcodeScheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive
  FROM OligoBarcodeScheme
  WHERE NOT EXISTS(SELECT * FROM OligoBarcodeScheme_Audit)
$$

--
-- Audit Triggers For OligoBarcodeScheme 
--


CREATE TRIGGER TrAI_OligoBarcodeScheme_FER AFTER INSERT ON OligoBarcodeScheme FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcodeScheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idOligoBarcodeScheme
  , NEW.oligoBarcodeScheme
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_OligoBarcodeScheme_FER AFTER UPDATE ON OligoBarcodeScheme FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcodeScheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idOligoBarcodeScheme
  , NEW.oligoBarcodeScheme
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_OligoBarcodeScheme_FER AFTER DELETE ON OligoBarcodeScheme FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcodeScheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idOligoBarcodeScheme
  , OLD.oligoBarcodeScheme
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For OligoBarcode 
--

-- select 'Creating table OligoBarcode'$$

-- DROP TABLE IF EXISTS `OligoBarcode_Audit`$$

CREATE TABLE IF NOT EXISTS `OligoBarcode_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idOligoBarcode`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`barcodeSequence`  varchar(20)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for OligoBarcode 
--

INSERT INTO OligoBarcode_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive
  FROM OligoBarcode
  WHERE NOT EXISTS(SELECT * FROM OligoBarcode_Audit)
$$

--
-- Audit Triggers For OligoBarcode 
--


CREATE TRIGGER TrAI_OligoBarcode_FER AFTER INSERT ON OligoBarcode FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcode_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idOligoBarcode
  , NEW.name
  , NEW.barcodeSequence
  , NEW.idOligoBarcodeScheme
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_OligoBarcode_FER AFTER UPDATE ON OligoBarcode FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcode_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idOligoBarcode
  , NEW.name
  , NEW.barcodeSequence
  , NEW.idOligoBarcodeScheme
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_OligoBarcode_FER AFTER DELETE ON OligoBarcode FOR EACH ROW
BEGIN
  INSERT INTO OligoBarcode_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idOligoBarcode
  , OLD.name
  , OLD.barcodeSequence
  , OLD.idOligoBarcodeScheme
  , OLD.sortOrder
  , OLD.isActive );
END;
$$


--
-- Audit Table For Organism 
--

-- select 'Creating table Organism'$$

-- DROP TABLE IF EXISTS `Organism_Audit`$$

CREATE TABLE IF NOT EXISTS `Organism_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`organism`  varchar(50)  NULL DEFAULT NULL
 ,`abbreviation`  varchar(10)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`das2Name`  varchar(200)  NULL DEFAULT NULL
 ,`sortOrder`  int(10) unsigned  NULL DEFAULT NULL
 ,`binomialName`  varchar(200)  NULL DEFAULT NULL
 ,`NCBITaxID`  varchar(45)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Organism 
--

INSERT INTO Organism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOrganism
  , organism
  , abbreviation
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , das2Name
  , sortOrder
  , binomialName
  , NCBITaxID )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idOrganism
  , organism
  , abbreviation
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , das2Name
  , sortOrder
  , binomialName
  , NCBITaxID
  FROM Organism
  WHERE NOT EXISTS(SELECT * FROM Organism_Audit)
$$

--
-- Audit Triggers For Organism 
--


CREATE TRIGGER TrAI_Organism_FER AFTER INSERT ON Organism FOR EACH ROW
BEGIN
  INSERT INTO Organism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOrganism
  , organism
  , abbreviation
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , das2Name
  , sortOrder
  , binomialName
  , NCBITaxID )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idOrganism
  , NEW.organism
  , NEW.abbreviation
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser
  , NEW.das2Name
  , NEW.sortOrder
  , NEW.binomialName
  , NEW.NCBITaxID );
END;
$$


CREATE TRIGGER TrAU_Organism_FER AFTER UPDATE ON Organism FOR EACH ROW
BEGIN
  INSERT INTO Organism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOrganism
  , organism
  , abbreviation
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , das2Name
  , sortOrder
  , binomialName
  , NCBITaxID )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idOrganism
  , NEW.organism
  , NEW.abbreviation
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser
  , NEW.das2Name
  , NEW.sortOrder
  , NEW.binomialName
  , NEW.NCBITaxID );
END;
$$


CREATE TRIGGER TrAD_Organism_FER AFTER DELETE ON Organism FOR EACH ROW
BEGIN
  INSERT INTO Organism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOrganism
  , organism
  , abbreviation
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , das2Name
  , sortOrder
  , binomialName
  , NCBITaxID )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idOrganism
  , OLD.organism
  , OLD.abbreviation
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive
  , OLD.idAppUser
  , OLD.das2Name
  , OLD.sortOrder
  , OLD.binomialName
  , OLD.NCBITaxID );
END;
$$


--
-- Audit Table For OtherAccountFieldsConfiguration 
--

-- select 'Creating table OtherAccountFieldsConfiguration'$$

-- DROP TABLE IF EXISTS `OtherAccountFieldsConfiguration_Audit`$$

CREATE TABLE IF NOT EXISTS `OtherAccountFieldsConfiguration_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idOtherAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for OtherAccountFieldsConfiguration 
--

INSERT INTO OtherAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired
  FROM OtherAccountFieldsConfiguration
  WHERE NOT EXISTS(SELECT * FROM OtherAccountFieldsConfiguration_Audit)
$$

--
-- Audit Triggers For OtherAccountFieldsConfiguration 
--


CREATE TRIGGER TrAI_OtherAccountFieldsConfiguration_FER AFTER INSERT ON OtherAccountFieldsConfiguration FOR EACH ROW
BEGIN
  INSERT INTO OtherAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idOtherAccountFieldsConfiguration
  , NEW.fieldName
  , NEW.include
  , NEW.isRequired );
END;
$$


CREATE TRIGGER TrAU_OtherAccountFieldsConfiguration_FER AFTER UPDATE ON OtherAccountFieldsConfiguration FOR EACH ROW
BEGIN
  INSERT INTO OtherAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idOtherAccountFieldsConfiguration
  , NEW.fieldName
  , NEW.include
  , NEW.isRequired );
END;
$$


CREATE TRIGGER TrAD_OtherAccountFieldsConfiguration_FER AFTER DELETE ON OtherAccountFieldsConfiguration FOR EACH ROW
BEGIN
  INSERT INTO OtherAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idOtherAccountFieldsConfiguration
  , OLD.fieldName
  , OLD.include
  , OLD.isRequired );
END;
$$


--
-- Audit Table For PipelineProtocol
--

-- select 'Creating table PipelineProtocol'$$

-- DROP TABLE IF EXISTS `PipelineProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `PipelineProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPipelineProtocol` int(10)      NULL DEFAULT NULL
 ,`description`        longtext     NULL DEFAULT NULL
 ,`idCoreFacility`     int(10)      NULL DEFAULT NULL
 ,`protocol`           varchar(50)  NULL DEFAULT NULL
 ,`isDefault`          varchar(1)   NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PipelineProtocol
--

INSERT INTO PipelineProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPipelineProtocol
  , description
  , idCoreFacility
  , protocol
  , isDefault )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPipelineProtocol
  , description
  , idCoreFacility
  , protocol
  , isDefault
  FROM PipelineProtocol
  WHERE NOT EXISTS(SELECT * FROM PipelineProtocol_Audit)
$$

--
-- Audit Triggers For PipelineProtocol
--


CREATE TRIGGER TrAI_PipelineProtocol_FER AFTER INSERT ON PipelineProtocol FOR EACH ROW
BEGIN
  INSERT INTO PipelineProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPipelineProtocol
  , description
  , idCoreFacility
  , protocol
  , isDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPipelineProtocol
  , NEW.description
  , NEW.idCoreFacility
  , NEW.protocol
  , NEW.isDefault );
END;
$$


CREATE TRIGGER TrAU_PipelineProtocol_FER AFTER UPDATE ON PipelineProtocol FOR EACH ROW
BEGIN
  INSERT INTO PipelineProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPipelineProtocol
  , description
  , idCoreFacility
  , protocol
  , isDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPipelineProtocol
  , NEW.description
  , NEW.idCoreFacility
  , NEW.protocol
  , NEW.isDefault );
END;
$$


CREATE TRIGGER TrAD_PipelineProtocol_FER AFTER DELETE ON PipelineProtocol FOR EACH ROW
BEGIN
  INSERT INTO PipelineProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPipelineProtocol
  , description
  , idCoreFacility
  , protocol
  , isDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPipelineProtocol
  , OLD.description
  , OLD.idCoreFacility
  , OLD.protocol
  , OLD.isDefault );
END;
$$
//////////////////////////////////////////////////////


--
-- Audit Table For PlateType
--

-- select 'Creating table PlateType'$$

-- DROP TABLE IF EXISTS `PlateType_Audit`$$

CREATE TABLE IF NOT EXISTS `PlateType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codePlateType`  varchar(10)  NULL DEFAULT NULL
 ,`plateTypeDescription`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PlateType
--

INSERT INTO PlateType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePlateType
  , plateTypeDescription
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codePlateType
  , plateTypeDescription
  , isActive
  FROM PlateType
  WHERE NOT EXISTS(SELECT * FROM PlateType_Audit)
$$

--
-- Audit Triggers For PlateType
--


CREATE TRIGGER TrAI_PlateType_FER AFTER INSERT ON PlateType FOR EACH ROW
BEGIN
  INSERT INTO PlateType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codePlateType
  , NEW.plateTypeDescription
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_PlateType_FER AFTER UPDATE ON PlateType FOR EACH ROW
BEGIN
  INSERT INTO PlateType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codePlateType
  , NEW.plateTypeDescription
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_PlateType_FER AFTER DELETE ON PlateType FOR EACH ROW
BEGIN
  INSERT INTO PlateType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codePlateType
  , OLD.plateTypeDescription
  , OLD.isActive );
END;
$$


--
-- Audit Table For PlateWell 
--

-- select 'Creating table PlateWell'$$

-- DROP TABLE IF EXISTS `PlateWell_Audit`$$

CREATE TABLE IF NOT EXISTS `PlateWell_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPlateWell`  int(10)  NULL DEFAULT NULL
 ,`row`  varchar(50)  NULL DEFAULT NULL
 ,`col`  int(10)  NULL DEFAULT NULL
 ,`ind`  int(10)  NULL DEFAULT NULL
 ,`idPlate`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`redoFlag`  char(1)  NULL DEFAULT NULL
 ,`isControl`  char(1)  NULL DEFAULT NULL
 ,`idAssay`  int(10)  NULL DEFAULT NULL
 ,`idPrimer`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PlateWell 
--

INSERT INTO PlateWell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlateWell
  , row
  , col
  , ind
  , idPlate
  , idSample
  , idRequest
  , createDate
  , codeReactionType
  , redoFlag
  , isControl
  , idAssay
  , idPrimer )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPlateWell
  , row
  , col
  , ind
  , idPlate
  , idSample
  , idRequest
  , createDate
  , codeReactionType
  , redoFlag
  , isControl
  , idAssay
  , idPrimer
  FROM PlateWell
  WHERE NOT EXISTS(SELECT * FROM PlateWell_Audit)
$$

--
-- Audit Triggers For PlateWell 
--


CREATE TRIGGER TrAI_PlateWell_FER AFTER INSERT ON PlateWell FOR EACH ROW
BEGIN
  INSERT INTO PlateWell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlateWell
  , row
  , col
  , ind
  , idPlate
  , idSample
  , idRequest
  , createDate
  , codeReactionType
  , redoFlag
  , isControl
  , idAssay
  , idPrimer )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPlateWell
  , NEW.row
  , NEW.col
  , NEW.ind
  , NEW.idPlate
  , NEW.idSample
  , NEW.idRequest
  , NEW.createDate
  , NEW.codeReactionType
  , NEW.redoFlag
  , NEW.isControl
  , NEW.idAssay
  , NEW.idPrimer );
END;
$$


CREATE TRIGGER TrAU_PlateWell_FER AFTER UPDATE ON PlateWell FOR EACH ROW
BEGIN
  INSERT INTO PlateWell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlateWell
  , row
  , col
  , ind
  , idPlate
  , idSample
  , idRequest
  , createDate
  , codeReactionType
  , redoFlag
  , isControl
  , idAssay
  , idPrimer )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPlateWell
  , NEW.row
  , NEW.col
  , NEW.ind
  , NEW.idPlate
  , NEW.idSample
  , NEW.idRequest
  , NEW.createDate
  , NEW.codeReactionType
  , NEW.redoFlag
  , NEW.isControl
  , NEW.idAssay
  , NEW.idPrimer );
END;
$$


CREATE TRIGGER TrAD_PlateWell_FER AFTER DELETE ON PlateWell FOR EACH ROW
BEGIN
  INSERT INTO PlateWell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlateWell
  , row
  , col
  , ind
  , idPlate
  , idSample
  , idRequest
  , createDate
  , codeReactionType
  , redoFlag
  , isControl
  , idAssay
  , idPrimer )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPlateWell
  , OLD.row
  , OLD.col
  , OLD.ind
  , OLD.idPlate
  , OLD.idSample
  , OLD.idRequest
  , OLD.createDate
  , OLD.codeReactionType
  , OLD.redoFlag
  , OLD.isControl
  , OLD.idAssay
  , OLD.idPrimer );
END;
$$


--
-- Audit Table For Plate 
--

-- select 'Creating table Plate'$$

-- DROP TABLE IF EXISTS `Plate_Audit`$$

CREATE TABLE IF NOT EXISTS `Plate_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPlate`  int(10)  NULL DEFAULT NULL
 ,`idInstrumentRun`  int(10)  NULL DEFAULT NULL
 ,`codePlateType`  varchar(10)  NULL DEFAULT NULL
 ,`quadrant`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`comments`  varchar(200)  NULL DEFAULT NULL
 ,`label`  varchar(50)  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`creator`  varchar(50)  NULL DEFAULT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Plate 
--

INSERT INTO Plate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlate
  , idInstrumentRun
  , codePlateType
  , quadrant
  , createDate
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPlate
  , idInstrumentRun
  , codePlateType
  , quadrant
  , createDate
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType
  FROM Plate
  WHERE NOT EXISTS(SELECT * FROM Plate_Audit)
$$

--
-- Audit Triggers For Plate 
--


CREATE TRIGGER TrAI_Plate_FER AFTER INSERT ON Plate FOR EACH ROW
BEGIN
  INSERT INTO Plate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlate
  , idInstrumentRun
  , codePlateType
  , quadrant
  , createDate
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPlate
  , NEW.idInstrumentRun
  , NEW.codePlateType
  , NEW.quadrant
  , NEW.createDate
  , NEW.comments
  , NEW.label
  , NEW.codeReactionType
  , NEW.creator
  , NEW.codeSealType );
END;
$$


CREATE TRIGGER TrAU_Plate_FER AFTER UPDATE ON Plate FOR EACH ROW
BEGIN
  INSERT INTO Plate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlate
  , idInstrumentRun
  , codePlateType
  , quadrant
  , createDate
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPlate
  , NEW.idInstrumentRun
  , NEW.codePlateType
  , NEW.quadrant
  , NEW.createDate
  , NEW.comments
  , NEW.label
  , NEW.codeReactionType
  , NEW.creator
  , NEW.codeSealType );
END;
$$


CREATE TRIGGER TrAD_Plate_FER AFTER DELETE ON Plate FOR EACH ROW
BEGIN
  INSERT INTO Plate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlate
  , idInstrumentRun
  , codePlateType
  , quadrant
  , createDate
  , comments
  , label
  , codeReactionType
  , creator
  , codeSealType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPlate
  , OLD.idInstrumentRun
  , OLD.codePlateType
  , OLD.quadrant
  , OLD.createDate
  , OLD.comments
  , OLD.label
  , OLD.codeReactionType
  , OLD.creator
  , OLD.codeSealType );
END;
$$


--
-- Audit Table For PriceCategoryStep 
--

-- select 'Creating table PriceCategoryStep'$$

-- DROP TABLE IF EXISTS `PriceCategoryStep_Audit`$$

CREATE TABLE IF NOT EXISTS `PriceCategoryStep_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PriceCategoryStep 
--

INSERT INTO PriceCategoryStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , codeStep )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPriceCategory
  , codeStep
  FROM PriceCategoryStep
  WHERE NOT EXISTS(SELECT * FROM PriceCategoryStep_Audit)
$$

--
-- Audit Triggers For PriceCategoryStep 
--


CREATE TRIGGER TrAI_PriceCategoryStep_FER AFTER INSERT ON PriceCategoryStep FOR EACH ROW
BEGIN
  INSERT INTO PriceCategoryStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , codeStep )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceCategory
  , NEW.codeStep );
END;
$$


CREATE TRIGGER TrAU_PriceCategoryStep_FER AFTER UPDATE ON PriceCategoryStep FOR EACH ROW
BEGIN
  INSERT INTO PriceCategoryStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , codeStep )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceCategory
  , NEW.codeStep );
END;
$$


CREATE TRIGGER TrAD_PriceCategoryStep_FER AFTER DELETE ON PriceCategoryStep FOR EACH ROW
BEGIN
  INSERT INTO PriceCategoryStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , codeStep )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPriceCategory
  , OLD.codeStep );
END;
$$


--
-- Audit Table For PriceCategory 
--

-- select 'Creating table PriceCategory'$$

-- DROP TABLE IF EXISTS `PriceCategory_Audit`$$

CREATE TABLE IF NOT EXISTS `PriceCategory_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`pluginClassName`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter1`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter2`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PriceCategory 
--

INSERT INTO PriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive
  FROM PriceCategory
  WHERE NOT EXISTS(SELECT * FROM PriceCategory_Audit)
$$

--
-- Audit Triggers For PriceCategory 
--


CREATE TRIGGER TrAI_PriceCategory_FER AFTER INSERT ON PriceCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceCategory
  , NEW.name
  , NEW.description
  , NEW.codeBillingChargeKind
  , NEW.pluginClassName
  , NEW.dictionaryClassNameFilter1
  , NEW.dictionaryClassNameFilter2
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_PriceCategory_FER AFTER UPDATE ON PriceCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceCategory
  , NEW.name
  , NEW.description
  , NEW.codeBillingChargeKind
  , NEW.pluginClassName
  , NEW.dictionaryClassNameFilter1
  , NEW.dictionaryClassNameFilter2
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_PriceCategory_FER AFTER DELETE ON PriceCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPriceCategory
  , OLD.name
  , OLD.description
  , OLD.codeBillingChargeKind
  , OLD.pluginClassName
  , OLD.dictionaryClassNameFilter1
  , OLD.dictionaryClassNameFilter2
  , OLD.isActive );
END;
$$


--
-- Audit Table For PriceCriteria 
--

-- select 'Creating table PriceCriteria'$$

-- DROP TABLE IF EXISTS `PriceCriteria_Audit`$$

CREATE TABLE IF NOT EXISTS `PriceCriteria_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPriceCriteria`  int(10)  NULL DEFAULT NULL
 ,`filter1`  varchar(20)  NULL DEFAULT NULL
 ,`filter2`  varchar(20)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PriceCriteria 
--

INSERT INTO PriceCriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice
  FROM PriceCriteria
  WHERE NOT EXISTS(SELECT * FROM PriceCriteria_Audit)
$$

--
-- Audit Triggers For PriceCriteria 
--


CREATE TRIGGER TrAI_PriceCriteria_FER AFTER INSERT ON PriceCriteria FOR EACH ROW
BEGIN
  INSERT INTO PriceCriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceCriteria
  , NEW.filter1
  , NEW.filter2
  , NEW.idPrice );
END;
$$


CREATE TRIGGER TrAU_PriceCriteria_FER AFTER UPDATE ON PriceCriteria FOR EACH ROW
BEGIN
  INSERT INTO PriceCriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceCriteria
  , NEW.filter1
  , NEW.filter2
  , NEW.idPrice );
END;
$$


CREATE TRIGGER TrAD_PriceCriteria_FER AFTER DELETE ON PriceCriteria FOR EACH ROW
BEGIN
  INSERT INTO PriceCriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPriceCriteria
  , OLD.filter1
  , OLD.filter2
  , OLD.idPrice );
END;
$$


--
-- Audit Table For PriceSheetPriceCategory 
--

-- select 'Creating table PriceSheetPriceCategory'$$

-- DROP TABLE IF EXISTS `PriceSheetPriceCategory_Audit`$$

CREATE TABLE IF NOT EXISTS `PriceSheetPriceCategory_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PriceSheetPriceCategory 
--

INSERT INTO PriceSheetPriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPriceSheet
  , idPriceCategory
  , sortOrder
  FROM PriceSheetPriceCategory
  WHERE NOT EXISTS(SELECT * FROM PriceSheetPriceCategory_Audit)
$$

--
-- Audit Triggers For PriceSheetPriceCategory 
--


CREATE TRIGGER TrAI_PriceSheetPriceCategory_FER AFTER INSERT ON PriceSheetPriceCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceSheetPriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceSheet
  , NEW.idPriceCategory
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_PriceSheetPriceCategory_FER AFTER UPDATE ON PriceSheetPriceCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceSheetPriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceSheet
  , NEW.idPriceCategory
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_PriceSheetPriceCategory_FER AFTER DELETE ON PriceSheetPriceCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceSheetPriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPriceSheet
  , OLD.idPriceCategory
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For PriceSheetRequestCategory 
--

-- select 'Creating table PriceSheetRequestCategory'$$

-- DROP TABLE IF EXISTS `PriceSheetRequestCategory_Audit`$$

CREATE TABLE IF NOT EXISTS `PriceSheetRequestCategory_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PriceSheetRequestCategory 
--

INSERT INTO PriceSheetRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , codeRequestCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPriceSheet
  , codeRequestCategory
  FROM PriceSheetRequestCategory
  WHERE NOT EXISTS(SELECT * FROM PriceSheetRequestCategory_Audit)
$$

--
-- Audit Triggers For PriceSheetRequestCategory 
--


CREATE TRIGGER TrAI_PriceSheetRequestCategory_FER AFTER INSERT ON PriceSheetRequestCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceSheetRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceSheet
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_PriceSheetRequestCategory_FER AFTER UPDATE ON PriceSheetRequestCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceSheetRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceSheet
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_PriceSheetRequestCategory_FER AFTER DELETE ON PriceSheetRequestCategory FOR EACH ROW
BEGIN
  INSERT INTO PriceSheetRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPriceSheet
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For PriceSheet 
--

-- select 'Creating table PriceSheet'$$

-- DROP TABLE IF EXISTS `PriceSheet_Audit`$$

CREATE TABLE IF NOT EXISTS `PriceSheet_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PriceSheet 
--

INSERT INTO PriceSheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , name
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPriceSheet
  , name
  , description
  , isActive
  FROM PriceSheet
  WHERE NOT EXISTS(SELECT * FROM PriceSheet_Audit)
$$

--
-- Audit Triggers For PriceSheet 
--


CREATE TRIGGER TrAI_PriceSheet_FER AFTER INSERT ON PriceSheet FOR EACH ROW
BEGIN
  INSERT INTO PriceSheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceSheet
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_PriceSheet_FER AFTER UPDATE ON PriceSheet FOR EACH ROW
BEGIN
  INSERT INTO PriceSheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPriceSheet
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_PriceSheet_FER AFTER DELETE ON PriceSheet FOR EACH ROW
BEGIN
  INSERT INTO PriceSheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPriceSheet
  , OLD.name
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For Price 
--

-- select 'Creating table Price'$$

-- DROP TABLE IF EXISTS `Price_Audit`$$

CREATE TABLE IF NOT EXISTS `Price_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`unitPriceExternalAcademic`  decimal(7,2)  NULL DEFAULT NULL
 ,`unitPriceExternalCommercial`  decimal(7,2)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Price 
--

INSERT INTO Price_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive
  FROM Price
  WHERE NOT EXISTS(SELECT * FROM Price_Audit)
$$

--
-- Audit Triggers For Price 
--


CREATE TRIGGER TrAI_Price_FER AFTER INSERT ON Price FOR EACH ROW
BEGIN
  INSERT INTO Price_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPrice
  , NEW.name
  , NEW.description
  , NEW.unitPrice
  , NEW.unitPriceExternalAcademic
  , NEW.unitPriceExternalCommercial
  , NEW.idPriceCategory
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Price_FER AFTER UPDATE ON Price FOR EACH ROW
BEGIN
  INSERT INTO Price_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPrice
  , NEW.name
  , NEW.description
  , NEW.unitPrice
  , NEW.unitPriceExternalAcademic
  , NEW.unitPriceExternalCommercial
  , NEW.idPriceCategory
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Price_FER AFTER DELETE ON Price FOR EACH ROW
BEGIN
  INSERT INTO Price_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPrice
  , OLD.name
  , OLD.description
  , OLD.unitPrice
  , OLD.unitPriceExternalAcademic
  , OLD.unitPriceExternalCommercial
  , OLD.idPriceCategory
  , OLD.isActive );
END;
$$


--
-- Audit Table For Primer 
--

-- select 'Creating table Primer'$$

-- DROP TABLE IF EXISTS `Primer_Audit`$$

CREATE TABLE IF NOT EXISTS `Primer_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPrimer`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`sequence`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Primer 
--

INSERT INTO Primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrimer
  , name
  , description
  , sequence
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPrimer
  , name
  , description
  , sequence
  , isActive
  FROM Primer
  WHERE NOT EXISTS(SELECT * FROM Primer_Audit)
$$

--
-- Audit Triggers For Primer 
--


CREATE TRIGGER TrAI_Primer_FER AFTER INSERT ON Primer FOR EACH ROW
BEGIN
  INSERT INTO Primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrimer
  , name
  , description
  , sequence
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPrimer
  , NEW.name
  , NEW.description
  , NEW.sequence
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Primer_FER AFTER UPDATE ON Primer FOR EACH ROW
BEGIN
  INSERT INTO Primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrimer
  , name
  , description
  , sequence
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPrimer
  , NEW.name
  , NEW.description
  , NEW.sequence
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Primer_FER AFTER DELETE ON Primer FOR EACH ROW
BEGIN
  INSERT INTO Primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPrimer
  , name
  , description
  , sequence
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPrimer
  , OLD.name
  , OLD.description
  , OLD.sequence
  , OLD.isActive );
END;
$$


--
-- Audit Table For ProductLedger 
--

-- select 'Creating table ProductLedger'$$

-- DROP TABLE IF EXISTS `ProductLedger_Audit`$$

CREATE TABLE IF NOT EXISTS `ProductLedger_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProductLedger`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`comment`  varchar(5000)  NULL DEFAULT NULL
 ,`timeStame`  datetime  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(5000)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ProductLedger 
--

INSERT INTO ProductLedger_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest
  , notes )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest
  , notes
  FROM ProductLedger
  WHERE NOT EXISTS(SELECT * FROM ProductLedger_Audit)
$$

--
-- Audit Triggers For ProductLedger 
--


CREATE TRIGGER TrAI_ProductLedger_FER AFTER INSERT ON ProductLedger FOR EACH ROW
BEGIN
  INSERT INTO ProductLedger_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest
  , notes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProductLedger
  , NEW.idLab
  , NEW.idProduct
  , NEW.qty
  , NEW.comment
  , NEW.timeStame
  , NEW.idProductOrder
  , NEW.idRequest
  , NEW.notes );
END;
$$


CREATE TRIGGER TrAU_ProductLedger_FER AFTER UPDATE ON ProductLedger FOR EACH ROW
BEGIN
  INSERT INTO ProductLedger_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest
  , notes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProductLedger
  , NEW.idLab
  , NEW.idProduct
  , NEW.qty
  , NEW.comment
  , NEW.timeStame
  , NEW.idProductOrder
  , NEW.idRequest
  , NEW.notes );
END;
$$


CREATE TRIGGER TrAD_ProductLedger_FER AFTER DELETE ON ProductLedger FOR EACH ROW
BEGIN
  INSERT INTO ProductLedger_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest
  , notes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProductLedger
  , OLD.idLab
  , OLD.idProduct
  , OLD.qty
  , OLD.comment
  , OLD.timeStame
  , OLD.idProductOrder
  , OLD.idRequest
  , OLD.notes );
END;
$$


--
-- Audit Table For ProductLineItem 
--

-- select 'Creating table ProductLineItem'$$

-- DROP TABLE IF EXISTS `ProductLineItem_Audit`$$

CREATE TABLE IF NOT EXISTS `ProductLineItem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProductLineItem`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`codeProductOrderStatus`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ProductLineItem 
--

INSERT INTO ProductLineItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus
  FROM ProductLineItem
  WHERE NOT EXISTS(SELECT * FROM ProductLineItem_Audit)
$$

--
-- Audit Triggers For ProductLineItem 
--


CREATE TRIGGER TrAI_ProductLineItem_FER AFTER INSERT ON ProductLineItem FOR EACH ROW
BEGIN
  INSERT INTO ProductLineItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProductLineItem
  , NEW.idProductOrder
  , NEW.idProduct
  , NEW.qty
  , NEW.unitPrice
  , NEW.codeProductOrderStatus );
END;
$$


CREATE TRIGGER TrAU_ProductLineItem_FER AFTER UPDATE ON ProductLineItem FOR EACH ROW
BEGIN
  INSERT INTO ProductLineItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProductLineItem
  , NEW.idProductOrder
  , NEW.idProduct
  , NEW.qty
  , NEW.unitPrice
  , NEW.codeProductOrderStatus );
END;
$$


CREATE TRIGGER TrAD_ProductLineItem_FER AFTER DELETE ON ProductLineItem FOR EACH ROW
BEGIN
  INSERT INTO ProductLineItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProductLineItem
  , OLD.idProductOrder
  , OLD.idProduct
  , OLD.qty
  , OLD.unitPrice
  , OLD.codeProductOrderStatus );
END;
$$


--
-- Audit Table For ProductOrderFile 
--

-- select 'Creating table ProductOrderFile'$$

-- DROP TABLE IF EXISTS `ProductOrderFile_Audit`$$

CREATE TABLE IF NOT EXISTS `ProductOrderFile_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProductOrderFile`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
 ,`baseFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(300)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ProductOrderFile 
--

INSERT INTO ProductOrderFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate
  , baseFilePath
  , qualifiedFilePath )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate
  , baseFilePath
  , qualifiedFilePath
  FROM ProductOrderFile
  WHERE NOT EXISTS(SELECT * FROM ProductOrderFile_Audit)
$$

--
-- Audit Triggers For ProductOrderFile 
--


CREATE TRIGGER TrAI_ProductOrderFile_FER AFTER INSERT ON ProductOrderFile FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate
  , baseFilePath
  , qualifiedFilePath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProductOrderFile
  , NEW.idProductOrder
  , NEW.fileName
  , NEW.fileSize
  , NEW.createDate
  , NEW.baseFilePath
  , NEW.qualifiedFilePath );
END;
$$


CREATE TRIGGER TrAU_ProductOrderFile_FER AFTER UPDATE ON ProductOrderFile FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate
  , baseFilePath
  , qualifiedFilePath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProductOrderFile
  , NEW.idProductOrder
  , NEW.fileName
  , NEW.fileSize
  , NEW.createDate
  , NEW.baseFilePath
  , NEW.qualifiedFilePath );
END;
$$


CREATE TRIGGER TrAD_ProductOrderFile_FER AFTER DELETE ON ProductOrderFile FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate
  , baseFilePath
  , qualifiedFilePath )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProductOrderFile
  , OLD.idProductOrder
  , OLD.fileName
  , OLD.fileSize
  , OLD.createDate
  , OLD.baseFilePath
  , OLD.qualifiedFilePath );
END;
$$


--
-- Audit Table For ProductOrderStatus 
--

-- select 'Creating table ProductOrderStatus'$$

-- DROP TABLE IF EXISTS `ProductOrderStatus_Audit`$$

CREATE TABLE IF NOT EXISTS `ProductOrderStatus_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeProductOrderStatus`  varchar(10)  NULL DEFAULT NULL
 ,`productOrderStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ProductOrderStatus 
--

INSERT INTO ProductOrderStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeProductOrderStatus
  , productOrderStatus
  , isActive
  FROM ProductOrderStatus
  WHERE NOT EXISTS(SELECT * FROM ProductOrderStatus_Audit)
$$

--
-- Audit Triggers For ProductOrderStatus 
--


CREATE TRIGGER TrAI_ProductOrderStatus_FER AFTER INSERT ON ProductOrderStatus FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeProductOrderStatus
  , NEW.productOrderStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_ProductOrderStatus_FER AFTER UPDATE ON ProductOrderStatus FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeProductOrderStatus
  , NEW.productOrderStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_ProductOrderStatus_FER AFTER DELETE ON ProductOrderStatus FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeProductOrderStatus
  , OLD.productOrderStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For ProductOrder 
--

-- select 'Creating table ProductOrder'$$

-- DROP TABLE IF EXISTS `ProductOrder_Audit`$$

CREATE TABLE IF NOT EXISTS `ProductOrder_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`submitDate`  datetime  NULL DEFAULT NULL
 ,`idProductType`  int(10)  NULL DEFAULT NULL
 ,`quoteNumber`  varchar(50)  NULL DEFAULT NULL
 ,`quoteReceivedDate`  datetime  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`productOrderNumber`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ProductOrder 
--

INSERT INTO ProductOrder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , idProductType
  , quoteNumber
  , quoteReceivedDate
  , idBillingAccount
  , productOrderNumber )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , idProductType
  , quoteNumber
  , quoteReceivedDate
  , idBillingAccount
  , productOrderNumber
  FROM ProductOrder
  WHERE NOT EXISTS(SELECT * FROM ProductOrder_Audit)
$$

--
-- Audit Triggers For ProductOrder 
--


CREATE TRIGGER TrAI_ProductOrder_FER AFTER INSERT ON ProductOrder FOR EACH ROW
BEGIN
  INSERT INTO ProductOrder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , idProductType
  , quoteNumber
  , quoteReceivedDate
  , idBillingAccount
  , productOrderNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProductOrder
  , NEW.idAppUser
  , NEW.idLab
  , NEW.idCoreFacility
  , NEW.submitDate
  , NEW.idProductType
  , NEW.quoteNumber
  , NEW.quoteReceivedDate
  , NEW.idBillingAccount
  , NEW.productOrderNumber );
END;
$$


CREATE TRIGGER TrAU_ProductOrder_FER AFTER UPDATE ON ProductOrder FOR EACH ROW
BEGIN
  INSERT INTO ProductOrder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , idProductType
  , quoteNumber
  , quoteReceivedDate
  , idBillingAccount
  , productOrderNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProductOrder
  , NEW.idAppUser
  , NEW.idLab
  , NEW.idCoreFacility
  , NEW.submitDate
  , NEW.idProductType
  , NEW.quoteNumber
  , NEW.quoteReceivedDate
  , NEW.idBillingAccount
  , NEW.productOrderNumber );
END;
$$


CREATE TRIGGER TrAD_ProductOrder_FER AFTER DELETE ON ProductOrder FOR EACH ROW
BEGIN
  INSERT INTO ProductOrder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , idProductType
  , quoteNumber
  , quoteReceivedDate
  , idBillingAccount
  , productOrderNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProductOrder
  , OLD.idAppUser
  , OLD.idLab
  , OLD.idCoreFacility
  , OLD.submitDate
  , OLD.idProductType
  , OLD.quoteNumber
  , OLD.quoteReceivedDate
  , OLD.idBillingAccount
  , OLD.productOrderNumber );
END;
$$


--
-- Audit Table For ProductType 
--

-- select 'Creating table ProductType'$$

-- DROP TABLE IF EXISTS `ProductType_Audit`$$

CREATE TABLE IF NOT EXISTS `ProductType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProductType`  int(10)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ProductType 
--

INSERT INTO ProductType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory
  FROM ProductType
  WHERE NOT EXISTS(SELECT * FROM ProductType_Audit)
$$

--
-- Audit Triggers For ProductType 
--


CREATE TRIGGER TrAI_ProductType_FER AFTER INSERT ON ProductType FOR EACH ROW
BEGIN
  INSERT INTO ProductType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProductType
  , NEW.description
  , NEW.idCoreFacility
  , NEW.idVendor
  , NEW.idPriceCategory );
END;
$$


CREATE TRIGGER TrAU_ProductType_FER AFTER UPDATE ON ProductType FOR EACH ROW
BEGIN
  INSERT INTO ProductType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProductType
  , NEW.description
  , NEW.idCoreFacility
  , NEW.idVendor
  , NEW.idPriceCategory );
END;
$$


CREATE TRIGGER TrAD_ProductType_FER AFTER DELETE ON ProductType FOR EACH ROW
BEGIN
  INSERT INTO ProductType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProductType
  , OLD.description
  , OLD.idCoreFacility
  , OLD.idVendor
  , OLD.idPriceCategory );
END;
$$


--
-- Audit Table For Product 
--

-- select 'Creating table Product'$$

-- DROP TABLE IF EXISTS `Product_Audit`$$

CREATE TABLE IF NOT EXISTS `Product_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`idProductType`  int(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`orderQty`  int(10)  NULL DEFAULT NULL
 ,`useQty`  int(10)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`batchSamplesByUseQuantity`  char(1)  NULL DEFAULT NULL
 ,`billThroughGnomex`  char(1)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Product 
--

INSERT INTO Product_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProduct
  , name
  , idProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity
  , billThroughGnomex
  , description )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProduct
  , name
  , idProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity
  , billThroughGnomex
  , description
  FROM Product
  WHERE NOT EXISTS(SELECT * FROM Product_Audit)
$$

--
-- Audit Triggers For Product 
--


CREATE TRIGGER TrAI_Product_FER AFTER INSERT ON Product FOR EACH ROW
BEGIN
  INSERT INTO Product_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProduct
  , name
  , idProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity
  , billThroughGnomex
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProduct
  , NEW.name
  , NEW.idProductType
  , NEW.idPrice
  , NEW.orderQty
  , NEW.useQty
  , NEW.catalogNumber
  , NEW.isActive
  , NEW.batchSamplesByUseQuantity
  , NEW.billThroughGnomex
  , NEW.description );
END;
$$


CREATE TRIGGER TrAU_Product_FER AFTER UPDATE ON Product FOR EACH ROW
BEGIN
  INSERT INTO Product_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProduct
  , name
  , idProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity
  , billThroughGnomex
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProduct
  , NEW.name
  , NEW.idProductType
  , NEW.idPrice
  , NEW.orderQty
  , NEW.useQty
  , NEW.catalogNumber
  , NEW.isActive
  , NEW.batchSamplesByUseQuantity
  , NEW.billThroughGnomex
  , NEW.description );
END;
$$


CREATE TRIGGER TrAD_Product_FER AFTER DELETE ON Product FOR EACH ROW
BEGIN
  INSERT INTO Product_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProduct
  , name
  , idProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity
  , billThroughGnomex
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProduct
  , OLD.name
  , OLD.idProductType
  , OLD.idPrice
  , OLD.orderQty
  , OLD.useQty
  , OLD.catalogNumber
  , OLD.isActive
  , OLD.batchSamplesByUseQuantity
  , OLD.billThroughGnomex
  , OLD.description );
END;
$$


--
-- Audit Table For Project 
--

-- select 'Creating table Project'$$

-- DROP TABLE IF EXISTS `Project_Audit`$$

CREATE TABLE IF NOT EXISTS `Project_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(4000)  NULL DEFAULT NULL
 ,`publicDateForAppUsers`  datetime  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Project 
--

INSERT INTO Project_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility
  FROM Project
  WHERE NOT EXISTS(SELECT * FROM Project_Audit)
$$

--
-- Audit Triggers For Project 
--


CREATE TRIGGER TrAI_Project_FER AFTER INSERT ON Project FOR EACH ROW
BEGIN
  INSERT INTO Project_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProject
  , NEW.name
  , NEW.description
  , NEW.publicDateForAppUsers
  , NEW.idLab
  , NEW.idAppUser
  , NEW.codeVisibility );
END;
$$


CREATE TRIGGER TrAU_Project_FER AFTER UPDATE ON Project FOR EACH ROW
BEGIN
  INSERT INTO Project_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProject
  , NEW.name
  , NEW.description
  , NEW.publicDateForAppUsers
  , NEW.idLab
  , NEW.idAppUser
  , NEW.codeVisibility );
END;
$$


CREATE TRIGGER TrAD_Project_FER AFTER DELETE ON Project FOR EACH ROW
BEGIN
  INSERT INTO Project_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProject
  , OLD.name
  , OLD.description
  , OLD.publicDateForAppUsers
  , OLD.idLab
  , OLD.idAppUser
  , OLD.codeVisibility );
END;
$$


--
-- Audit Table For PropertyAnalysisType 
--

-- select 'Creating table PropertyAnalysisType'$$

-- DROP TABLE IF EXISTS `PropertyAnalysisType_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyAnalysisType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyAnalysisType 
--

INSERT INTO PropertyAnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAnalysisType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProperty
  , idAnalysisType
  FROM PropertyAnalysisType
  WHERE NOT EXISTS(SELECT * FROM PropertyAnalysisType_Audit)
$$

--
-- Audit Triggers For PropertyAnalysisType 
--


CREATE TRIGGER TrAI_PropertyAnalysisType_FER AFTER INSERT ON PropertyAnalysisType FOR EACH ROW
BEGIN
  INSERT INTO PropertyAnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAnalysisType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.idAnalysisType );
END;
$$


CREATE TRIGGER TrAU_PropertyAnalysisType_FER AFTER UPDATE ON PropertyAnalysisType FOR EACH ROW
BEGIN
  INSERT INTO PropertyAnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAnalysisType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.idAnalysisType );
END;
$$


CREATE TRIGGER TrAD_PropertyAnalysisType_FER AFTER DELETE ON PropertyAnalysisType FOR EACH ROW
BEGIN
  INSERT INTO PropertyAnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAnalysisType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProperty
  , OLD.idAnalysisType );
END;
$$


--
-- Audit Table For PropertyAppUser 
--

-- select 'Creating table PropertyAppUser'$$

-- DROP TABLE IF EXISTS `PropertyAppUser_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyAppUser_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyAppUser 
--

INSERT INTO PropertyAppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProperty
  , idAppUser
  FROM PropertyAppUser
  WHERE NOT EXISTS(SELECT * FROM PropertyAppUser_Audit)
$$

--
-- Audit Triggers For PropertyAppUser 
--


CREATE TRIGGER TrAI_PropertyAppUser_FER AFTER INSERT ON PropertyAppUser FOR EACH ROW
BEGIN
  INSERT INTO PropertyAppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_PropertyAppUser_FER AFTER UPDATE ON PropertyAppUser FOR EACH ROW
BEGIN
  INSERT INTO PropertyAppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_PropertyAppUser_FER AFTER DELETE ON PropertyAppUser FOR EACH ROW
BEGIN
  INSERT INTO PropertyAppUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProperty
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For PropertyDictionary 
--

-- select 'Creating table PropertyDictionary'$$

-- DROP TABLE IF EXISTS `PropertyDictionary_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyDictionary_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPropertyDictionary`  int(10)  NULL DEFAULT NULL
 ,`propertyName`  varchar(200)  NULL DEFAULT NULL
 ,`propertyValue`  varchar(2000)  NULL DEFAULT NULL
 ,`propertyDescription`  varchar(2000)  NULL DEFAULT NULL
 ,`forServerOnly`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyDictionary 
--

INSERT INTO PropertyDictionary_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory
  FROM PropertyDictionary
  WHERE NOT EXISTS(SELECT * FROM PropertyDictionary_Audit)
$$

--
-- Audit Triggers For PropertyDictionary 
--


CREATE TRIGGER TrAI_PropertyDictionary_FER AFTER INSERT ON PropertyDictionary FOR EACH ROW
BEGIN
  INSERT INTO PropertyDictionary_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyDictionary
  , NEW.propertyName
  , NEW.propertyValue
  , NEW.propertyDescription
  , NEW.forServerOnly
  , NEW.idCoreFacility
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_PropertyDictionary_FER AFTER UPDATE ON PropertyDictionary FOR EACH ROW
BEGIN
  INSERT INTO PropertyDictionary_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyDictionary
  , NEW.propertyName
  , NEW.propertyValue
  , NEW.propertyDescription
  , NEW.forServerOnly
  , NEW.idCoreFacility
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_PropertyDictionary_FER AFTER DELETE ON PropertyDictionary FOR EACH ROW
BEGIN
  INSERT INTO PropertyDictionary_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPropertyDictionary
  , OLD.propertyName
  , OLD.propertyValue
  , OLD.propertyDescription
  , OLD.forServerOnly
  , OLD.idCoreFacility
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For PropertyEntryOption 
--

-- select 'Creating table PropertyEntryOption'$$

-- DROP TABLE IF EXISTS `PropertyEntryOption_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyEntryOption_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyEntryOption 
--

INSERT INTO PropertyEntryOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idPropertyOption )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPropertyEntry
  , idPropertyOption
  FROM PropertyEntryOption
  WHERE NOT EXISTS(SELECT * FROM PropertyEntryOption_Audit)
$$

--
-- Audit Triggers For PropertyEntryOption 
--


CREATE TRIGGER TrAI_PropertyEntryOption_FER AFTER INSERT ON PropertyEntryOption FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntryOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyEntry
  , NEW.idPropertyOption );
END;
$$


CREATE TRIGGER TrAU_PropertyEntryOption_FER AFTER UPDATE ON PropertyEntryOption FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntryOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyEntry
  , NEW.idPropertyOption );
END;
$$


CREATE TRIGGER TrAD_PropertyEntryOption_FER AFTER DELETE ON PropertyEntryOption FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntryOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPropertyEntry
  , OLD.idPropertyOption );
END;
$$


--
-- Audit Table For PropertyEntryValue 
--

-- select 'Creating table PropertyEntryValue'$$

-- DROP TABLE IF EXISTS `PropertyEntryValue_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyEntryValue_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPropertyEntryValue`  int(10) unsigned  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyEntryValue 
--

INSERT INTO PropertyEntryValue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPropertyEntryValue
  , value
  , idPropertyEntry
  FROM PropertyEntryValue
  WHERE NOT EXISTS(SELECT * FROM PropertyEntryValue_Audit)
$$

--
-- Audit Triggers For PropertyEntryValue 
--


CREATE TRIGGER TrAI_PropertyEntryValue_FER AFTER INSERT ON PropertyEntryValue FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntryValue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyEntryValue
  , NEW.value
  , NEW.idPropertyEntry );
END;
$$


CREATE TRIGGER TrAU_PropertyEntryValue_FER AFTER UPDATE ON PropertyEntryValue FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntryValue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyEntryValue
  , NEW.value
  , NEW.idPropertyEntry );
END;
$$


CREATE TRIGGER TrAD_PropertyEntryValue_FER AFTER DELETE ON PropertyEntryValue FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntryValue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPropertyEntryValue
  , OLD.value
  , OLD.idPropertyEntry );
END;
$$


--
-- Audit Table For PropertyEntry 
--

-- select 'Creating table PropertyEntry'$$

-- DROP TABLE IF EXISTS `PropertyEntry_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyEntry_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(2000)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyEntry 
--

INSERT INTO PropertyEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest
  FROM PropertyEntry
  WHERE NOT EXISTS(SELECT * FROM PropertyEntry_Audit)
$$

--
-- Audit Triggers For PropertyEntry 
--


CREATE TRIGGER TrAI_PropertyEntry_FER AFTER INSERT ON PropertyEntry FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyEntry
  , NEW.idProperty
  , NEW.idSample
  , NEW.valueString
  , NEW.otherLabel
  , NEW.idDataTrack
  , NEW.idAnalysis
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAU_PropertyEntry_FER AFTER UPDATE ON PropertyEntry FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyEntry
  , NEW.idProperty
  , NEW.idSample
  , NEW.valueString
  , NEW.otherLabel
  , NEW.idDataTrack
  , NEW.idAnalysis
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAD_PropertyEntry_FER AFTER DELETE ON PropertyEntry FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPropertyEntry
  , OLD.idProperty
  , OLD.idSample
  , OLD.valueString
  , OLD.otherLabel
  , OLD.idDataTrack
  , OLD.idAnalysis
  , OLD.idRequest );
END;
$$


--
-- Audit Table For PropertyOption 
--

-- select 'Creating table PropertyOption'$$

-- DROP TABLE IF EXISTS `PropertyOption_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyOption_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyOption 
--

INSERT INTO PropertyOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive
  FROM PropertyOption
  WHERE NOT EXISTS(SELECT * FROM PropertyOption_Audit)
$$

--
-- Audit Triggers For PropertyOption 
--


CREATE TRIGGER TrAI_PropertyOption_FER AFTER INSERT ON PropertyOption FOR EACH ROW
BEGIN
  INSERT INTO PropertyOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyOption
  , NEW.value
  , NEW.idProperty
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_PropertyOption_FER AFTER UPDATE ON PropertyOption FOR EACH ROW
BEGIN
  INSERT INTO PropertyOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPropertyOption
  , NEW.value
  , NEW.idProperty
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_PropertyOption_FER AFTER DELETE ON PropertyOption FOR EACH ROW
BEGIN
  INSERT INTO PropertyOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPropertyOption
  , OLD.value
  , OLD.idProperty
  , OLD.sortOrder
  , OLD.isActive );
END;
$$


--
-- Audit Table For PropertyOrganism 
--

-- select 'Creating table PropertyOrganism'$$

-- DROP TABLE IF EXISTS `PropertyOrganism_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyOrganism_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyOrganism 
--

INSERT INTO PropertyOrganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idOrganism )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProperty
  , idOrganism
  FROM PropertyOrganism
  WHERE NOT EXISTS(SELECT * FROM PropertyOrganism_Audit)
$$

--
-- Audit Triggers For PropertyOrganism 
--


CREATE TRIGGER TrAI_PropertyOrganism_FER AFTER INSERT ON PropertyOrganism FOR EACH ROW
BEGIN
  INSERT INTO PropertyOrganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAU_PropertyOrganism_FER AFTER UPDATE ON PropertyOrganism FOR EACH ROW
BEGIN
  INSERT INTO PropertyOrganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAD_PropertyOrganism_FER AFTER DELETE ON PropertyOrganism FOR EACH ROW
BEGIN
  INSERT INTO PropertyOrganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProperty
  , OLD.idOrganism );
END;
$$


--
-- Audit Table For PropertyPlatformApplication 
--

-- select 'Creating table PropertyPlatformApplication'$$

-- DROP TABLE IF EXISTS `PropertyPlatformApplication_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyPlatformApplication_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idPlatformApplication`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyPlatformApplication 
--

INSERT INTO PropertyPlatformApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication
  FROM PropertyPlatformApplication
  WHERE NOT EXISTS(SELECT * FROM PropertyPlatformApplication_Audit)
$$

--
-- Audit Triggers For PropertyPlatformApplication 
--


CREATE TRIGGER TrAI_PropertyPlatformApplication_FER AFTER INSERT ON PropertyPlatformApplication FOR EACH ROW
BEGIN
  INSERT INTO PropertyPlatformApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idPlatformApplication
  , NEW.idProperty
  , NEW.codeRequestCategory
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAU_PropertyPlatformApplication_FER AFTER UPDATE ON PropertyPlatformApplication FOR EACH ROW
BEGIN
  INSERT INTO PropertyPlatformApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idPlatformApplication
  , NEW.idProperty
  , NEW.codeRequestCategory
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAD_PropertyPlatformApplication_FER AFTER DELETE ON PropertyPlatformApplication FOR EACH ROW
BEGIN
  INSERT INTO PropertyPlatformApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idPlatformApplication
  , OLD.idProperty
  , OLD.codeRequestCategory
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For PropertyType 
--

-- select 'Creating table PropertyType'$$

-- DROP TABLE IF EXISTS `PropertyType_Audit`$$

CREATE TABLE IF NOT EXISTS `PropertyType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codePropertyType`  varchar(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for PropertyType 
--

INSERT INTO PropertyType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePropertyType
  , name
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codePropertyType
  , name
  , isActive
  FROM PropertyType
  WHERE NOT EXISTS(SELECT * FROM PropertyType_Audit)
$$

--
-- Audit Triggers For PropertyType 
--


CREATE TRIGGER TrAI_PropertyType_FER AFTER INSERT ON PropertyType FOR EACH ROW
BEGIN
  INSERT INTO PropertyType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codePropertyType
  , NEW.name
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_PropertyType_FER AFTER UPDATE ON PropertyType FOR EACH ROW
BEGIN
  INSERT INTO PropertyType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codePropertyType
  , NEW.name
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_PropertyType_FER AFTER DELETE ON PropertyType FOR EACH ROW
BEGIN
  INSERT INTO PropertyType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codePropertyType
  , OLD.name
  , OLD.isActive );
END;
$$


--
-- Audit Table For Property 
--

-- select 'Creating table Property'$$

-- DROP TABLE IF EXISTS `Property_Audit`$$

CREATE TABLE IF NOT EXISTS `Property_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
 ,`forSample`  char(1)  NULL DEFAULT NULL
 ,`forAnalysis`  char(1)  NULL DEFAULT NULL
 ,`forDataTrack`  char(1)  NULL DEFAULT NULL
 ,`codePropertyType`  varchar(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`forRequest`  char(1)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Property 
--

INSERT INTO Property_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , name
  , description
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , isRequired
  , forSample
  , forAnalysis
  , forDataTrack
  , codePropertyType
  , sortOrder
  , idCoreFacility
  , forRequest
  , idPriceCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idProperty
  , name
  , description
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , isRequired
  , forSample
  , forAnalysis
  , forDataTrack
  , codePropertyType
  , sortOrder
  , idCoreFacility
  , forRequest
  , idPriceCategory
  FROM Property
  WHERE NOT EXISTS(SELECT * FROM Property_Audit)
$$

--
-- Audit Triggers For Property 
--


CREATE TRIGGER TrAI_Property_FER AFTER INSERT ON Property FOR EACH ROW
BEGIN
  INSERT INTO Property_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , name
  , description
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , isRequired
  , forSample
  , forAnalysis
  , forDataTrack
  , codePropertyType
  , sortOrder
  , idCoreFacility
  , forRequest
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.name
  , NEW.description
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser
  , NEW.isRequired
  , NEW.forSample
  , NEW.forAnalysis
  , NEW.forDataTrack
  , NEW.codePropertyType
  , NEW.sortOrder
  , NEW.idCoreFacility
  , NEW.forRequest
  , NEW.idPriceCategory );
END;
$$


CREATE TRIGGER TrAU_Property_FER AFTER UPDATE ON Property FOR EACH ROW
BEGIN
  INSERT INTO Property_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , name
  , description
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , isRequired
  , forSample
  , forAnalysis
  , forDataTrack
  , codePropertyType
  , sortOrder
  , idCoreFacility
  , forRequest
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idProperty
  , NEW.name
  , NEW.description
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser
  , NEW.isRequired
  , NEW.forSample
  , NEW.forAnalysis
  , NEW.forDataTrack
  , NEW.codePropertyType
  , NEW.sortOrder
  , NEW.idCoreFacility
  , NEW.forRequest
  , NEW.idPriceCategory );
END;
$$


CREATE TRIGGER TrAD_Property_FER AFTER DELETE ON Property FOR EACH ROW
BEGIN
  INSERT INTO Property_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idProperty
  , name
  , description
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser
  , isRequired
  , forSample
  , forAnalysis
  , forDataTrack
  , codePropertyType
  , sortOrder
  , idCoreFacility
  , forRequest
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idProperty
  , OLD.name
  , OLD.description
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive
  , OLD.idAppUser
  , OLD.isRequired
  , OLD.forSample
  , OLD.forAnalysis
  , OLD.forDataTrack
  , OLD.codePropertyType
  , OLD.sortOrder
  , OLD.idCoreFacility
  , OLD.forRequest
  , OLD.idPriceCategory );
END;
$$


--
-- Audit Table For ProtocolType 
--

-- select 'Creating table ProtocolType'$$

-- DROP TABLE IF EXISTS `ProtocolType_Audit`$$

CREATE TABLE IF NOT EXISTS `ProtocolType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeProtocolType`  varchar(10)  NULL DEFAULT NULL
 ,`protocolType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ProtocolType 
--

INSERT INTO ProtocolType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProtocolType
  , protocolType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeProtocolType
  , protocolType
  , isActive
  FROM ProtocolType
  WHERE NOT EXISTS(SELECT * FROM ProtocolType_Audit)
$$

--
-- Audit Triggers For ProtocolType 
--


CREATE TRIGGER TrAI_ProtocolType_FER AFTER INSERT ON ProtocolType FOR EACH ROW
BEGIN
  INSERT INTO ProtocolType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeProtocolType
  , NEW.protocolType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_ProtocolType_FER AFTER UPDATE ON ProtocolType FOR EACH ROW
BEGIN
  INSERT INTO ProtocolType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeProtocolType
  , NEW.protocolType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_ProtocolType_FER AFTER DELETE ON ProtocolType FOR EACH ROW
BEGIN
  INSERT INTO ProtocolType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeProtocolType
  , OLD.protocolType
  , OLD.isActive );
END;
$$


--
-- Audit Table For QualityControlStepEntry 
--

-- select 'Creating table QualityControlStepEntry'$$

-- DROP TABLE IF EXISTS `QualityControlStepEntry_Audit`$$

CREATE TABLE IF NOT EXISTS `QualityControlStepEntry_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idQualityControlStepEntry`  int(10)  NULL DEFAULT NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for QualityControlStepEntry 
--

INSERT INTO QualityControlStepEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel
  FROM QualityControlStepEntry
  WHERE NOT EXISTS(SELECT * FROM QualityControlStepEntry_Audit)
$$

--
-- Audit Triggers For QualityControlStepEntry 
--


CREATE TRIGGER TrAI_QualityControlStepEntry_FER AFTER INSERT ON QualityControlStepEntry FOR EACH ROW
BEGIN
  INSERT INTO QualityControlStepEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idQualityControlStepEntry
  , NEW.codeQualityControlStep
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_QualityControlStepEntry_FER AFTER UPDATE ON QualityControlStepEntry FOR EACH ROW
BEGIN
  INSERT INTO QualityControlStepEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idQualityControlStepEntry
  , NEW.codeQualityControlStep
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_QualityControlStepEntry_FER AFTER DELETE ON QualityControlStepEntry FOR EACH ROW
BEGIN
  INSERT INTO QualityControlStepEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idQualityControlStepEntry
  , OLD.codeQualityControlStep
  , OLD.idProject
  , OLD.valueString
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For QualityControlStep 
--

-- select 'Creating table QualityControlStep'$$

-- DROP TABLE IF EXISTS `QualityControlStep_Audit`$$

CREATE TABLE IF NOT EXISTS `QualityControlStep_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`qualityControlStep`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for QualityControlStep 
--

INSERT INTO QualityControlStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  FROM QualityControlStep
  WHERE NOT EXISTS(SELECT * FROM QualityControlStep_Audit)
$$

--
-- Audit Triggers For QualityControlStep 
--


CREATE TRIGGER TrAI_QualityControlStep_FER AFTER INSERT ON QualityControlStep FOR EACH ROW
BEGIN
  INSERT INTO QualityControlStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeQualityControlStep
  , NEW.qualityControlStep
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_QualityControlStep_FER AFTER UPDATE ON QualityControlStep FOR EACH ROW
BEGIN
  INSERT INTO QualityControlStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeQualityControlStep
  , NEW.qualityControlStep
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_QualityControlStep_FER AFTER DELETE ON QualityControlStep FOR EACH ROW
BEGIN
  INSERT INTO QualityControlStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeQualityControlStep
  , OLD.qualityControlStep
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive );
END;
$$


--
-- Audit Table For ReactionType 
--

-- select 'Creating table ReactionType'$$

-- DROP TABLE IF EXISTS `ReactionType_Audit`$$

CREATE TABLE IF NOT EXISTS `ReactionType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`reactionType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ReactionType 
--

INSERT INTO ReactionType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeReactionType
  , reactionType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeReactionType
  , reactionType
  , isActive
  FROM ReactionType
  WHERE NOT EXISTS(SELECT * FROM ReactionType_Audit)
$$

--
-- Audit Triggers For ReactionType 
--


CREATE TRIGGER TrAI_ReactionType_FER AFTER INSERT ON ReactionType FOR EACH ROW
BEGIN
  INSERT INTO ReactionType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeReactionType
  , NEW.reactionType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_ReactionType_FER AFTER UPDATE ON ReactionType FOR EACH ROW
BEGIN
  INSERT INTO ReactionType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeReactionType
  , NEW.reactionType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_ReactionType_FER AFTER DELETE ON ReactionType FOR EACH ROW
BEGIN
  INSERT INTO ReactionType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeReactionType
  , OLD.reactionType
  , OLD.isActive );
END;
$$


--
-- Audit Table For RequestCategoryApplication 
--

-- select 'Creating table RequestCategoryApplication'$$

-- DROP TABLE IF EXISTS `RequestCategoryApplication_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestCategoryApplication_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idLabelingProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocolDefault`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestCategoryApplication 
--

INSERT INTO RequestCategoryApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault
  FROM RequestCategoryApplication
  WHERE NOT EXISTS(SELECT * FROM RequestCategoryApplication_Audit)
$$

--
-- Audit Triggers For RequestCategoryApplication 
--


CREATE TRIGGER TrAI_RequestCategoryApplication_FER AFTER INSERT ON RequestCategoryApplication FOR EACH ROW
BEGIN
  INSERT INTO RequestCategoryApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.idLabelingProtocolDefault
  , NEW.idHybProtocolDefault
  , NEW.idScanProtocolDefault
  , NEW.idFeatureExtractionProtocolDefault );
END;
$$


CREATE TRIGGER TrAU_RequestCategoryApplication_FER AFTER UPDATE ON RequestCategoryApplication FOR EACH ROW
BEGIN
  INSERT INTO RequestCategoryApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.idLabelingProtocolDefault
  , NEW.idHybProtocolDefault
  , NEW.idScanProtocolDefault
  , NEW.idFeatureExtractionProtocolDefault );
END;
$$


CREATE TRIGGER TrAD_RequestCategoryApplication_FER AFTER DELETE ON RequestCategoryApplication FOR EACH ROW
BEGIN
  INSERT INTO RequestCategoryApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeRequestCategory
  , OLD.codeApplication
  , OLD.idLabelingProtocolDefault
  , OLD.idHybProtocolDefault
  , OLD.idScanProtocolDefault
  , OLD.idFeatureExtractionProtocolDefault );
END;
$$


--
-- Audit Table For RequestCategoryType 
--

-- select 'Creating table RequestCategoryType'$$

-- DROP TABLE IF EXISTS `RequestCategoryType_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestCategoryType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeRequestCategoryType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(50)  NULL DEFAULT NULL
 ,`defaultIcon`  varchar(100)  NULL DEFAULT NULL
 ,`isIllumina`  char(1)  NULL DEFAULT NULL
 ,`hasChannels`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestCategoryType 
--

INSERT INTO RequestCategoryType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels
  FROM RequestCategoryType
  WHERE NOT EXISTS(SELECT * FROM RequestCategoryType_Audit)
$$

--
-- Audit Triggers For RequestCategoryType 
--


CREATE TRIGGER TrAI_RequestCategoryType_FER AFTER INSERT ON RequestCategoryType FOR EACH ROW
BEGIN
  INSERT INTO RequestCategoryType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestCategoryType
  , NEW.description
  , NEW.defaultIcon
  , NEW.isIllumina
  , NEW.hasChannels );
END;
$$


CREATE TRIGGER TrAU_RequestCategoryType_FER AFTER UPDATE ON RequestCategoryType FOR EACH ROW
BEGIN
  INSERT INTO RequestCategoryType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestCategoryType
  , NEW.description
  , NEW.defaultIcon
  , NEW.isIllumina
  , NEW.hasChannels );
END;
$$


CREATE TRIGGER TrAD_RequestCategoryType_FER AFTER DELETE ON RequestCategoryType FOR EACH ROW
BEGIN
  INSERT INTO RequestCategoryType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeRequestCategoryType
  , OLD.description
  , OLD.defaultIcon
  , OLD.isIllumina
  , OLD.hasChannels );
END;
$$


--
-- Audit Table For RequestCategory 
--

-- select 'Creating table RequestCategory'$$

-- DROP TABLE IF EXISTS `RequestCategory_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestCategory_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`requestCategory`  varchar(50)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`numberOfChannels`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`icon`  varchar(200)  NULL DEFAULT NULL
 ,`type`  varchar(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`isInternal`  char(1)  NULL DEFAULT NULL
 ,`isExternal`  char(1)  NULL DEFAULT NULL
 ,`refrainFromAutoDelete`  char(1)  NULL DEFAULT NULL
 ,`isClinicalResearch`  char(1)  NULL DEFAULT NULL
 ,`isOwnerOnly`  char(1)  NULL DEFAULT NULL
 ,`sampleBatchSize`  int(10)  NULL DEFAULT NULL
 ,`idProductType`  int(10)  NULL DEFAULT NULL
 ,`associatedWithAnalysis`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestCategory 
--

INSERT INTO RequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , requestCategory
  , idVendor
  , isActive
  , numberOfChannels
  , notes
  , icon
  , type
  , sortOrder
  , idOrganism
  , idCoreFacility
  , isInternal
  , isExternal
  , refrainFromAutoDelete
  , isClinicalResearch
  , isOwnerOnly
  , sampleBatchSize
  , idProductType
  , associatedWithAnalysis )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeRequestCategory
  , requestCategory
  , idVendor
  , isActive
  , numberOfChannels
  , notes
  , icon
  , type
  , sortOrder
  , idOrganism
  , idCoreFacility
  , isInternal
  , isExternal
  , refrainFromAutoDelete
  , isClinicalResearch
  , isOwnerOnly
  , sampleBatchSize
  , idProductType
  , associatedWithAnalysis
  FROM RequestCategory
  WHERE NOT EXISTS(SELECT * FROM RequestCategory_Audit)
$$

--
-- Audit Triggers For RequestCategory 
--


CREATE TRIGGER TrAI_RequestCategory_FER AFTER INSERT ON RequestCategory FOR EACH ROW
BEGIN
  INSERT INTO RequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , requestCategory
  , idVendor
  , isActive
  , numberOfChannels
  , notes
  , icon
  , type
  , sortOrder
  , idOrganism
  , idCoreFacility
  , isInternal
  , isExternal
  , refrainFromAutoDelete
  , isClinicalResearch
  , isOwnerOnly
  , sampleBatchSize
  , idProductType
  , associatedWithAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestCategory
  , NEW.requestCategory
  , NEW.idVendor
  , NEW.isActive
  , NEW.numberOfChannels
  , NEW.notes
  , NEW.icon
  , NEW.type
  , NEW.sortOrder
  , NEW.idOrganism
  , NEW.idCoreFacility
  , NEW.isInternal
  , NEW.isExternal
  , NEW.refrainFromAutoDelete
  , NEW.isClinicalResearch
  , NEW.isOwnerOnly
  , NEW.sampleBatchSize
  , NEW.idProductType
  , NEW.associatedWithAnalysis );
END;
$$


CREATE TRIGGER TrAU_RequestCategory_FER AFTER UPDATE ON RequestCategory FOR EACH ROW
BEGIN
  INSERT INTO RequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , requestCategory
  , idVendor
  , isActive
  , numberOfChannels
  , notes
  , icon
  , type
  , sortOrder
  , idOrganism
  , idCoreFacility
  , isInternal
  , isExternal
  , refrainFromAutoDelete
  , isClinicalResearch
  , isOwnerOnly
  , sampleBatchSize
  , idProductType
  , associatedWithAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestCategory
  , NEW.requestCategory
  , NEW.idVendor
  , NEW.isActive
  , NEW.numberOfChannels
  , NEW.notes
  , NEW.icon
  , NEW.type
  , NEW.sortOrder
  , NEW.idOrganism
  , NEW.idCoreFacility
  , NEW.isInternal
  , NEW.isExternal
  , NEW.refrainFromAutoDelete
  , NEW.isClinicalResearch
  , NEW.isOwnerOnly
  , NEW.sampleBatchSize
  , NEW.idProductType
  , NEW.associatedWithAnalysis );
END;
$$


CREATE TRIGGER TrAD_RequestCategory_FER AFTER DELETE ON RequestCategory FOR EACH ROW
BEGIN
  INSERT INTO RequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestCategory
  , requestCategory
  , idVendor
  , isActive
  , numberOfChannels
  , notes
  , icon
  , type
  , sortOrder
  , idOrganism
  , idCoreFacility
  , isInternal
  , isExternal
  , refrainFromAutoDelete
  , isClinicalResearch
  , isOwnerOnly
  , sampleBatchSize
  , idProductType
  , associatedWithAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeRequestCategory
  , OLD.requestCategory
  , OLD.idVendor
  , OLD.isActive
  , OLD.numberOfChannels
  , OLD.notes
  , OLD.icon
  , OLD.type
  , OLD.sortOrder
  , OLD.idOrganism
  , OLD.idCoreFacility
  , OLD.isInternal
  , OLD.isExternal
  , OLD.refrainFromAutoDelete
  , OLD.isClinicalResearch
  , OLD.isOwnerOnly
  , OLD.sampleBatchSize
  , OLD.idProductType
  , OLD.associatedWithAnalysis );
END;
$$


--
-- Audit Table For RequestCollaborator 
--

-- select 'Creating table RequestCollaborator'$$

-- DROP TABLE IF EXISTS `RequestCollaborator_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestCollaborator 
--

INSERT INTO RequestCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate
  FROM RequestCollaborator
  WHERE NOT EXISTS(SELECT * FROM RequestCollaborator_Audit)
$$

--
-- Audit Triggers For RequestCollaborator 
--


CREATE TRIGGER TrAI_RequestCollaborator_FER AFTER INSERT ON RequestCollaborator FOR EACH ROW
BEGIN
  INSERT INTO RequestCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAU_RequestCollaborator_FER AFTER UPDATE ON RequestCollaborator FOR EACH ROW
BEGIN
  INSERT INTO RequestCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAD_RequestCollaborator_FER AFTER DELETE ON RequestCollaborator FOR EACH ROW
BEGIN
  INSERT INTO RequestCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idRequest
  , OLD.idAppUser
  , OLD.canUploadData
  , OLD.canUpdate );
END;
$$


--
-- Audit Table For RequestHybridization 
--

-- select 'Creating table RequestHybridization'$$

-- DROP TABLE IF EXISTS `RequestHybridization_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestHybridization_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestHybridization 
--

INSERT INTO RequestHybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idHybridization )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idRequest
  , idHybridization
  FROM RequestHybridization
  WHERE NOT EXISTS(SELECT * FROM RequestHybridization_Audit)
$$

--
-- Audit Triggers For RequestHybridization 
--


CREATE TRIGGER TrAI_RequestHybridization_FER AFTER INSERT ON RequestHybridization FOR EACH ROW
BEGIN
  INSERT INTO RequestHybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idHybridization )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.idHybridization );
END;
$$


CREATE TRIGGER TrAU_RequestHybridization_FER AFTER UPDATE ON RequestHybridization FOR EACH ROW
BEGIN
  INSERT INTO RequestHybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idHybridization )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.idHybridization );
END;
$$


CREATE TRIGGER TrAD_RequestHybridization_FER AFTER DELETE ON RequestHybridization FOR EACH ROW
BEGIN
  INSERT INTO RequestHybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idHybridization )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idRequest
  , OLD.idHybridization );
END;
$$


--
-- Audit Table For RequestSeqLibTreatment 
--

-- select 'Creating table RequestSeqLibTreatment'$$

-- DROP TABLE IF EXISTS `RequestSeqLibTreatment_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestSeqLibTreatment_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestSeqLibTreatment 
--

INSERT INTO RequestSeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idSeqLibTreatment )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idRequest
  , idSeqLibTreatment
  FROM RequestSeqLibTreatment
  WHERE NOT EXISTS(SELECT * FROM RequestSeqLibTreatment_Audit)
$$

--
-- Audit Triggers For RequestSeqLibTreatment 
--


CREATE TRIGGER TrAI_RequestSeqLibTreatment_FER AFTER INSERT ON RequestSeqLibTreatment FOR EACH ROW
BEGIN
  INSERT INTO RequestSeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.idSeqLibTreatment );
END;
$$


CREATE TRIGGER TrAU_RequestSeqLibTreatment_FER AFTER UPDATE ON RequestSeqLibTreatment FOR EACH ROW
BEGIN
  INSERT INTO RequestSeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.idSeqLibTreatment );
END;
$$


CREATE TRIGGER TrAD_RequestSeqLibTreatment_FER AFTER DELETE ON RequestSeqLibTreatment FOR EACH ROW
BEGIN
  INSERT INTO RequestSeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idRequest
  , OLD.idSeqLibTreatment );
END;
$$


--
-- Audit Table For RequestStatus 
--

-- select 'Creating table RequestStatus'$$

-- DROP TABLE IF EXISTS `RequestStatus_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestStatus_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeRequestStatus`  varchar(10)  NULL DEFAULT NULL
 ,`requestStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestStatus 
--

INSERT INTO RequestStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestStatus
  , requestStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeRequestStatus
  , requestStatus
  , isActive
  FROM RequestStatus
  WHERE NOT EXISTS(SELECT * FROM RequestStatus_Audit)
$$

--
-- Audit Triggers For RequestStatus 
--


CREATE TRIGGER TrAI_RequestStatus_FER AFTER INSERT ON RequestStatus FOR EACH ROW
BEGIN
  INSERT INTO RequestStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestStatus
  , NEW.requestStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_RequestStatus_FER AFTER UPDATE ON RequestStatus FOR EACH ROW
BEGIN
  INSERT INTO RequestStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeRequestStatus
  , NEW.requestStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_RequestStatus_FER AFTER DELETE ON RequestStatus FOR EACH ROW
BEGIN
  INSERT INTO RequestStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeRequestStatus
  , OLD.requestStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For RequestToTopic 
--

-- select 'Creating table RequestToTopic'$$

-- DROP TABLE IF EXISTS `RequestToTopic_Audit`$$

CREATE TABLE IF NOT EXISTS `RequestToTopic_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for RequestToTopic 
--

INSERT INTO RequestToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idRequest )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idTopic
  , idRequest
  FROM RequestToTopic
  WHERE NOT EXISTS(SELECT * FROM RequestToTopic_Audit)
$$

--
-- Audit Triggers For RequestToTopic 
--


CREATE TRIGGER TrAI_RequestToTopic_FER AFTER INSERT ON RequestToTopic FOR EACH ROW
BEGIN
  INSERT INTO RequestToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAU_RequestToTopic_FER AFTER UPDATE ON RequestToTopic FOR EACH ROW
BEGIN
  INSERT INTO RequestToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAD_RequestToTopic_FER AFTER DELETE ON RequestToTopic FOR EACH ROW
BEGIN
  INSERT INTO RequestToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idTopic
  , OLD.idRequest );
END;
$$


--
-- Audit Table For Request 
--

-- select 'Creating table Request'$$

-- DROP TABLE IF EXISTS `Request_Audit`$$

CREATE TABLE IF NOT EXISTS `Request_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(50)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeProtocolType`  varchar(10)  NULL DEFAULT NULL
 ,`protocolNumber`  varchar(100)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`idSampleTypeDefault`  int(10)  NULL DEFAULT NULL
 ,`idOrganismSampleDefault`  int(10)  NULL DEFAULT NULL
 ,`idSampleSourceDefault`  int(10)  NULL DEFAULT NULL
 ,`idSamplePrepMethodDefault`  int(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`notes`  varchar(2000)  NULL DEFAULT NULL
 ,`completedDate`  datetime  NULL DEFAULT NULL
 ,`isArrayINFORequest`  char(1)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`lastModifyDate`  datetime  NULL DEFAULT NULL
 ,`isExternal`  char(1)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`privacyExpirationDate`  datetime  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`corePrepInstructions`  varchar(5000)  NULL DEFAULT NULL
 ,`analysisInstructions`  varchar(5000)  NULL DEFAULT NULL
 ,`captureLibDesignId`  varchar(200)  NULL DEFAULT NULL
 ,`avgInsertSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeTo`  int(10)  NULL DEFAULT NULL
 ,`idSampleDropOffLocation`  int(10)  NULL DEFAULT NULL
 ,`codeRequestStatus`  varchar(10)  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
 ,`coreToExtractDNA`  char(1)  NULL DEFAULT NULL
 ,`applicationNotes`  varchar(5000)  NULL DEFAULT NULL
 ,`processingDate`  datetime  NULL DEFAULT NULL
 ,`codeIsolationPrepType`  varchar(15)  NULL DEFAULT NULL
 ,`bioinformaticsAssist`  char(1)  NULL DEFAULT NULL
 ,`hasPrePooledLibraries`  char(1)  NULL DEFAULT NULL
 ,`numPrePooledTubes`  int(10)  NULL DEFAULT NULL
 ,`includeBisulfideConversion`  char(1)  NULL DEFAULT NULL
 ,`includeQubitConcentration`  char(1)  NULL DEFAULT NULL
 ,`adminNotes`  varchar(5000)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`archived`  char(1)  NULL DEFAULT NULL
 ,`reagent`  varchar(50)  NULL DEFAULT NULL
 ,`elutionBuffer`  varchar(50)  NULL DEFAULT NULL
 ,`usedDnase`  char(1)  NULL DEFAULT NULL
 ,`usedRnase`  char(1)  NULL DEFAULT NULL
 ,`keepSamples`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Request 
--

INSERT INTO Request_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , number
  , createDate
  , codeProtocolType
  , protocolNumber
  , idLab
  , idAppUser
  , idBillingAccount
  , codeRequestCategory
  , codeApplication
  , idProject
  , idSlideProduct
  , idSampleTypeDefault
  , idOrganismSampleDefault
  , idSampleSourceDefault
  , idSamplePrepMethodDefault
  , codeBioanalyzerChipType
  , notes
  , completedDate
  , isArrayINFORequest
  , codeVisibility
  , lastModifyDate
  , isExternal
  , idInstitution
  , idCoreFacility
  , name
  , privacyExpirationDate
  , description
  , corePrepInstructions
  , analysisInstructions
  , captureLibDesignId
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , idSampleDropOffLocation
  , codeRequestStatus
  , idSubmitter
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , adminNotes
  , idProduct
  , archived
  , reagent
  , elutionBuffer
  , usedDnase
  , usedRnase
  , keepSamples )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idRequest
  , number
  , createDate
  , codeProtocolType
  , protocolNumber
  , idLab
  , idAppUser
  , idBillingAccount
  , codeRequestCategory
  , codeApplication
  , idProject
  , idSlideProduct
  , idSampleTypeDefault
  , idOrganismSampleDefault
  , idSampleSourceDefault
  , idSamplePrepMethodDefault
  , codeBioanalyzerChipType
  , notes
  , completedDate
  , isArrayINFORequest
  , codeVisibility
  , lastModifyDate
  , isExternal
  , idInstitution
  , idCoreFacility
  , name
  , privacyExpirationDate
  , description
  , corePrepInstructions
  , analysisInstructions
  , captureLibDesignId
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , idSampleDropOffLocation
  , codeRequestStatus
  , idSubmitter
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , adminNotes
  , idProduct
  , archived
  , reagent
  , elutionBuffer
  , usedDnase
  , usedRnase
  , keepSamples
  FROM Request
  WHERE NOT EXISTS(SELECT * FROM Request_Audit)
$$

--
-- Audit Triggers For Request 
--


CREATE TRIGGER TrAI_Request_FER AFTER INSERT ON Request FOR EACH ROW
BEGIN
  INSERT INTO Request_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , number
  , createDate
  , codeProtocolType
  , protocolNumber
  , idLab
  , idAppUser
  , idBillingAccount
  , codeRequestCategory
  , codeApplication
  , idProject
  , idSlideProduct
  , idSampleTypeDefault
  , idOrganismSampleDefault
  , idSampleSourceDefault
  , idSamplePrepMethodDefault
  , codeBioanalyzerChipType
  , notes
  , completedDate
  , isArrayINFORequest
  , codeVisibility
  , lastModifyDate
  , isExternal
  , idInstitution
  , idCoreFacility
  , name
  , privacyExpirationDate
  , description
  , corePrepInstructions
  , analysisInstructions
  , captureLibDesignId
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , idSampleDropOffLocation
  , codeRequestStatus
  , idSubmitter
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , adminNotes
  , idProduct
  , archived
  , reagent
  , elutionBuffer
  , usedDnase
  , usedRnase
  , keepSamples )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.number
  , NEW.createDate
  , NEW.codeProtocolType
  , NEW.protocolNumber
  , NEW.idLab
  , NEW.idAppUser
  , NEW.idBillingAccount
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.idProject
  , NEW.idSlideProduct
  , NEW.idSampleTypeDefault
  , NEW.idOrganismSampleDefault
  , NEW.idSampleSourceDefault
  , NEW.idSamplePrepMethodDefault
  , NEW.codeBioanalyzerChipType
  , NEW.notes
  , NEW.completedDate
  , NEW.isArrayINFORequest
  , NEW.codeVisibility
  , NEW.lastModifyDate
  , NEW.isExternal
  , NEW.idInstitution
  , NEW.idCoreFacility
  , NEW.name
  , NEW.privacyExpirationDate
  , NEW.description
  , NEW.corePrepInstructions
  , NEW.analysisInstructions
  , NEW.captureLibDesignId
  , NEW.avgInsertSizeFrom
  , NEW.avgInsertSizeTo
  , NEW.idSampleDropOffLocation
  , NEW.codeRequestStatus
  , NEW.idSubmitter
  , NEW.coreToExtractDNA
  , NEW.applicationNotes
  , NEW.processingDate
  , NEW.codeIsolationPrepType
  , NEW.bioinformaticsAssist
  , NEW.hasPrePooledLibraries
  , NEW.numPrePooledTubes
  , NEW.includeBisulfideConversion
  , NEW.includeQubitConcentration
  , NEW.adminNotes
  , NEW.idProduct
  , NEW.archived
  , NEW.reagent
  , NEW.elutionBuffer
  , NEW.usedDnase
  , NEW.usedRnase
  , NEW.keepSamples );
END;
$$


CREATE TRIGGER TrAU_Request_FER AFTER UPDATE ON Request FOR EACH ROW
BEGIN
  INSERT INTO Request_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , number
  , createDate
  , codeProtocolType
  , protocolNumber
  , idLab
  , idAppUser
  , idBillingAccount
  , codeRequestCategory
  , codeApplication
  , idProject
  , idSlideProduct
  , idSampleTypeDefault
  , idOrganismSampleDefault
  , idSampleSourceDefault
  , idSamplePrepMethodDefault
  , codeBioanalyzerChipType
  , notes
  , completedDate
  , isArrayINFORequest
  , codeVisibility
  , lastModifyDate
  , isExternal
  , idInstitution
  , idCoreFacility
  , name
  , privacyExpirationDate
  , description
  , corePrepInstructions
  , analysisInstructions
  , captureLibDesignId
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , idSampleDropOffLocation
  , codeRequestStatus
  , idSubmitter
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , adminNotes
  , idProduct
  , archived
  , reagent
  , elutionBuffer
  , usedDnase
  , usedRnase
  , keepSamples )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idRequest
  , NEW.number
  , NEW.createDate
  , NEW.codeProtocolType
  , NEW.protocolNumber
  , NEW.idLab
  , NEW.idAppUser
  , NEW.idBillingAccount
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.idProject
  , NEW.idSlideProduct
  , NEW.idSampleTypeDefault
  , NEW.idOrganismSampleDefault
  , NEW.idSampleSourceDefault
  , NEW.idSamplePrepMethodDefault
  , NEW.codeBioanalyzerChipType
  , NEW.notes
  , NEW.completedDate
  , NEW.isArrayINFORequest
  , NEW.codeVisibility
  , NEW.lastModifyDate
  , NEW.isExternal
  , NEW.idInstitution
  , NEW.idCoreFacility
  , NEW.name
  , NEW.privacyExpirationDate
  , NEW.description
  , NEW.corePrepInstructions
  , NEW.analysisInstructions
  , NEW.captureLibDesignId
  , NEW.avgInsertSizeFrom
  , NEW.avgInsertSizeTo
  , NEW.idSampleDropOffLocation
  , NEW.codeRequestStatus
  , NEW.idSubmitter
  , NEW.coreToExtractDNA
  , NEW.applicationNotes
  , NEW.processingDate
  , NEW.codeIsolationPrepType
  , NEW.bioinformaticsAssist
  , NEW.hasPrePooledLibraries
  , NEW.numPrePooledTubes
  , NEW.includeBisulfideConversion
  , NEW.includeQubitConcentration
  , NEW.adminNotes
  , NEW.idProduct
  , NEW.archived
  , NEW.reagent
  , NEW.elutionBuffer
  , NEW.usedDnase
  , NEW.usedRnase
  , NEW.keepSamples );
END;
$$


CREATE TRIGGER TrAD_Request_FER AFTER DELETE ON Request FOR EACH ROW
BEGIN
  INSERT INTO Request_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idRequest
  , number
  , createDate
  , codeProtocolType
  , protocolNumber
  , idLab
  , idAppUser
  , idBillingAccount
  , codeRequestCategory
  , codeApplication
  , idProject
  , idSlideProduct
  , idSampleTypeDefault
  , idOrganismSampleDefault
  , idSampleSourceDefault
  , idSamplePrepMethodDefault
  , codeBioanalyzerChipType
  , notes
  , completedDate
  , isArrayINFORequest
  , codeVisibility
  , lastModifyDate
  , isExternal
  , idInstitution
  , idCoreFacility
  , name
  , privacyExpirationDate
  , description
  , corePrepInstructions
  , analysisInstructions
  , captureLibDesignId
  , avgInsertSizeFrom
  , avgInsertSizeTo
  , idSampleDropOffLocation
  , codeRequestStatus
  , idSubmitter
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , adminNotes
  , idProduct
  , archived
  , reagent
  , elutionBuffer
  , usedDnase
  , usedRnase
  , keepSamples )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idRequest
  , OLD.number
  , OLD.createDate
  , OLD.codeProtocolType
  , OLD.protocolNumber
  , OLD.idLab
  , OLD.idAppUser
  , OLD.idBillingAccount
  , OLD.codeRequestCategory
  , OLD.codeApplication
  , OLD.idProject
  , OLD.idSlideProduct
  , OLD.idSampleTypeDefault
  , OLD.idOrganismSampleDefault
  , OLD.idSampleSourceDefault
  , OLD.idSamplePrepMethodDefault
  , OLD.codeBioanalyzerChipType
  , OLD.notes
  , OLD.completedDate
  , OLD.isArrayINFORequest
  , OLD.codeVisibility
  , OLD.lastModifyDate
  , OLD.isExternal
  , OLD.idInstitution
  , OLD.idCoreFacility
  , OLD.name
  , OLD.privacyExpirationDate
  , OLD.description
  , OLD.corePrepInstructions
  , OLD.analysisInstructions
  , OLD.captureLibDesignId
  , OLD.avgInsertSizeFrom
  , OLD.avgInsertSizeTo
  , OLD.idSampleDropOffLocation
  , OLD.codeRequestStatus
  , OLD.idSubmitter
  , OLD.coreToExtractDNA
  , OLD.applicationNotes
  , OLD.processingDate
  , OLD.codeIsolationPrepType
  , OLD.bioinformaticsAssist
  , OLD.hasPrePooledLibraries
  , OLD.numPrePooledTubes
  , OLD.includeBisulfideConversion
  , OLD.includeQubitConcentration
  , OLD.adminNotes
  , OLD.idProduct
  , OLD.archived
  , OLD.reagent
  , OLD.elutionBuffer
  , OLD.usedDnase
  , OLD.usedRnase
  , OLD.keepSamples );
END;
$$


--
-- Audit Table For Sampledropofflocation 
--

-- select 'Creating table Sampledropofflocation'$$

-- DROP TABLE IF EXISTS `Sampledropofflocation_Audit`$$

CREATE TABLE IF NOT EXISTS `Sampledropofflocation_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSampleDropOffLocation`  int(10)  NULL DEFAULT NULL
 ,`sampleDropOffLocation`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Sampledropofflocation 
--

INSERT INTO Sampledropofflocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive
  FROM Sampledropofflocation
  WHERE NOT EXISTS(SELECT * FROM Sampledropofflocation_Audit)
$$

--
-- Audit Triggers For Sampledropofflocation 
--


CREATE TRIGGER TrAI_Sampledropofflocation_FER AFTER INSERT ON Sampledropofflocation FOR EACH ROW
BEGIN
  INSERT INTO Sampledropofflocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleDropOffLocation
  , NEW.sampleDropOffLocation
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Sampledropofflocation_FER AFTER UPDATE ON Sampledropofflocation FOR EACH ROW
BEGIN
  INSERT INTO Sampledropofflocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleDropOffLocation
  , NEW.sampleDropOffLocation
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Sampledropofflocation_FER AFTER DELETE ON Sampledropofflocation FOR EACH ROW
BEGIN
  INSERT INTO Sampledropofflocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSampleDropOffLocation
  , OLD.sampleDropOffLocation
  , OLD.isActive );
END;
$$


--
-- Audit Table For Sampleexperimentfile 
--

-- select 'Creating table Sampleexperimentfile'$$

-- DROP TABLE IF EXISTS `Sampleexperimentfile_Audit`$$

CREATE TABLE IF NOT EXISTS `Sampleexperimentfile_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSampleExperimentFile`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idExpFileRead1`  int(10)  NULL DEFAULT NULL
 ,`idExpFileRead2`  int(10)  NULL DEFAULT NULL
 ,`seqRunNumber`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Sampleexperimentfile 
--

INSERT INTO Sampleexperimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber
  FROM Sampleexperimentfile
  WHERE NOT EXISTS(SELECT * FROM Sampleexperimentfile_Audit)
$$

--
-- Audit Triggers For Sampleexperimentfile 
--


CREATE TRIGGER TrAI_Sampleexperimentfile_FER AFTER INSERT ON Sampleexperimentfile FOR EACH ROW
BEGIN
  INSERT INTO Sampleexperimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleExperimentFile
  , NEW.idSample
  , NEW.idExpFileRead1
  , NEW.idExpFileRead2
  , NEW.seqRunNumber );
END;
$$


CREATE TRIGGER TrAU_Sampleexperimentfile_FER AFTER UPDATE ON Sampleexperimentfile FOR EACH ROW
BEGIN
  INSERT INTO Sampleexperimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleExperimentFile
  , NEW.idSample
  , NEW.idExpFileRead1
  , NEW.idExpFileRead2
  , NEW.seqRunNumber );
END;
$$


CREATE TRIGGER TrAD_Sampleexperimentfile_FER AFTER DELETE ON Sampleexperimentfile FOR EACH ROW
BEGIN
  INSERT INTO Sampleexperimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSampleExperimentFile
  , OLD.idSample
  , OLD.idExpFileRead1
  , OLD.idExpFileRead2
  , OLD.seqRunNumber );
END;
$$


--
-- Audit Table For Samplefiletype 
--

-- select 'Creating table Samplefiletype'$$

-- DROP TABLE IF EXISTS `Samplefiletype_Audit`$$

CREATE TABLE IF NOT EXISTS `Samplefiletype_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeSampleFileType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Samplefiletype 
--

INSERT INTO Samplefiletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSampleFileType
  , description )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeSampleFileType
  , description
  FROM Samplefiletype
  WHERE NOT EXISTS(SELECT * FROM Samplefiletype_Audit)
$$

--
-- Audit Triggers For Samplefiletype 
--


CREATE TRIGGER TrAI_Samplefiletype_FER AFTER INSERT ON Samplefiletype FOR EACH ROW
BEGIN
  INSERT INTO Samplefiletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSampleFileType
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeSampleFileType
  , NEW.description );
END;
$$


CREATE TRIGGER TrAU_Samplefiletype_FER AFTER UPDATE ON Samplefiletype FOR EACH ROW
BEGIN
  INSERT INTO Samplefiletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSampleFileType
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeSampleFileType
  , NEW.description );
END;
$$


CREATE TRIGGER TrAD_Samplefiletype_FER AFTER DELETE ON Samplefiletype FOR EACH ROW
BEGIN
  INSERT INTO Samplefiletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSampleFileType
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeSampleFileType
  , OLD.description );
END;
$$


--
-- Audit Table For Sampleprepmethod 
--

-- select 'Creating table Sampleprepmethod'$$

-- DROP TABLE IF EXISTS `Sampleprepmethod_Audit`$$

CREATE TABLE IF NOT EXISTS `Sampleprepmethod_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSamplePrepMethod`  int(10)  NULL DEFAULT NULL
 ,`samplePrepMethod`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Sampleprepmethod 
--

INSERT INTO Sampleprepmethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive
  FROM Sampleprepmethod
  WHERE NOT EXISTS(SELECT * FROM Sampleprepmethod_Audit)
$$

--
-- Audit Triggers For Sampleprepmethod 
--


CREATE TRIGGER TrAI_Sampleprepmethod_FER AFTER INSERT ON Sampleprepmethod FOR EACH ROW
BEGIN
  INSERT INTO Sampleprepmethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSamplePrepMethod
  , NEW.samplePrepMethod
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Sampleprepmethod_FER AFTER UPDATE ON Sampleprepmethod FOR EACH ROW
BEGIN
  INSERT INTO Sampleprepmethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSamplePrepMethod
  , NEW.samplePrepMethod
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Sampleprepmethod_FER AFTER DELETE ON Sampleprepmethod FOR EACH ROW
BEGIN
  INSERT INTO Sampleprepmethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSamplePrepMethod
  , OLD.samplePrepMethod
  , OLD.isActive );
END;
$$


--
-- Audit Table For Samplesource 
--

-- select 'Creating table Samplesource'$$

-- DROP TABLE IF EXISTS `Samplesource_Audit`$$

CREATE TABLE IF NOT EXISTS `Samplesource_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSampleSource`  int(10)  NULL DEFAULT NULL
 ,`sampleSource`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Samplesource 
--

INSERT INTO Samplesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleSource
  , sampleSource
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSampleSource
  , sampleSource
  , isActive
  FROM Samplesource
  WHERE NOT EXISTS(SELECT * FROM Samplesource_Audit)
$$

--
-- Audit Triggers For Samplesource 
--


CREATE TRIGGER TrAI_Samplesource_FER AFTER INSERT ON Samplesource FOR EACH ROW
BEGIN
  INSERT INTO Samplesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleSource
  , NEW.sampleSource
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Samplesource_FER AFTER UPDATE ON Samplesource FOR EACH ROW
BEGIN
  INSERT INTO Samplesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleSource
  , NEW.sampleSource
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Samplesource_FER AFTER DELETE ON Samplesource FOR EACH ROW
BEGIN
  INSERT INTO Samplesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSampleSource
  , OLD.sampleSource
  , OLD.isActive );
END;
$$


--
-- Audit Table For SampleTypeRequestCategory 
--

-- select 'Creating table SampleTypeRequestCategory'$$

-- DROP TABLE IF EXISTS `SampleTypeRequestCategory_Audit`$$

CREATE TABLE IF NOT EXISTS `SampleTypeRequestCategory_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSampleTypeRequestCategory`  int(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SampleTypeRequestCategory 
--

INSERT INTO SampleTypeRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory
  FROM SampleTypeRequestCategory
  WHERE NOT EXISTS(SELECT * FROM SampleTypeRequestCategory_Audit)
$$

--
-- Audit Triggers For SampleTypeRequestCategory 
--


CREATE TRIGGER TrAI_SampleTypeRequestCategory_FER AFTER INSERT ON SampleTypeRequestCategory FOR EACH ROW
BEGIN
  INSERT INTO SampleTypeRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleTypeRequestCategory
  , NEW.idSampleType
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_SampleTypeRequestCategory_FER AFTER UPDATE ON SampleTypeRequestCategory FOR EACH ROW
BEGIN
  INSERT INTO SampleTypeRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleTypeRequestCategory
  , NEW.idSampleType
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_SampleTypeRequestCategory_FER AFTER DELETE ON SampleTypeRequestCategory FOR EACH ROW
BEGIN
  INSERT INTO SampleTypeRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSampleTypeRequestCategory
  , OLD.idSampleType
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For SampleType 
--

-- select 'Creating table SampleType'$$

-- DROP TABLE IF EXISTS `SampleType_Audit`$$

CREATE TABLE IF NOT EXISTS `SampleType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`sampleType`  varchar(50)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
 ,`notes`  varchar(5000)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SampleType 
--

INSERT INTO SampleType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility
  FROM SampleType
  WHERE NOT EXISTS(SELECT * FROM SampleType_Audit)
$$

--
-- Audit Triggers For SampleType 
--


CREATE TRIGGER TrAI_SampleType_FER AFTER INSERT ON SampleType FOR EACH ROW
BEGIN
  INSERT INTO SampleType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleType
  , NEW.sampleType
  , NEW.sortOrder
  , NEW.isActive
  , NEW.codeNucleotideType
  , NEW.notes
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_SampleType_FER AFTER UPDATE ON SampleType FOR EACH ROW
BEGIN
  INSERT INTO SampleType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSampleType
  , NEW.sampleType
  , NEW.sortOrder
  , NEW.isActive
  , NEW.codeNucleotideType
  , NEW.notes
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_SampleType_FER AFTER DELETE ON SampleType FOR EACH ROW
BEGIN
  INSERT INTO SampleType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSampleType
  , OLD.sampleType
  , OLD.sortOrder
  , OLD.isActive
  , OLD.codeNucleotideType
  , OLD.notes
  , OLD.idCoreFacility );
END;
$$


--
-- Audit Table For Sample 
--

-- select 'Creating table Sample'$$

-- DROP TABLE IF EXISTS `Sample_Audit`$$

CREATE TABLE IF NOT EXISTS `Sample_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`concentration`  decimal(8,3)  NULL DEFAULT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`otherOrganism`  varchar(100)  NULL DEFAULT NULL
 ,`idSampleSource`  int(10)  NULL DEFAULT NULL
 ,`idSamplePrepMethod`  int(10)  NULL DEFAULT NULL
 ,`otherSamplePrepMethod`  varchar(300)  NULL DEFAULT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`idOligoBarcode`  int(10)  NULL DEFAULT NULL
 ,`qualDate`  datetime  NULL DEFAULT NULL
 ,`qualFailed`  char(1)  NULL DEFAULT NULL
 ,`qualBypassed`  char(1)  NULL DEFAULT NULL
 ,`qual260nmTo280nmRatio`  decimal(3,2)  NULL DEFAULT NULL
 ,`qual260nmTo230nmRatio`  decimal(3,2)  NULL DEFAULT NULL
 ,`qualCalcConcentration`  decimal(8,2)  NULL DEFAULT NULL
 ,`qual28sTo18sRibosomalRatio`  decimal(2,1)  NULL DEFAULT NULL
 ,`qualRINNumber`  varchar(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`fragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`fragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepByCore`  char(1)  NULL DEFAULT NULL
 ,`seqPrepDate`  datetime  NULL DEFAULT NULL
 ,`seqPrepFailed`  char(1)  NULL DEFAULT NULL
 ,`seqPrepBypassed`  char(1)  NULL DEFAULT NULL
 ,`qualFragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`qualFragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepLibConcentration`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepQualCodeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`seqPrepGelFragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`seqPrepGelFragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepStockLibVol`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepStockEBVol`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepStockDate`  datetime  NULL DEFAULT NULL
 ,`seqPrepStockFailed`  char(1)  NULL DEFAULT NULL
 ,`seqPrepStockBypassed`  char(1)  NULL DEFAULT NULL
 ,`prepInstructions`  varchar(2000)  NULL DEFAULT NULL
 ,`ccNumber`  varchar(20)  NULL DEFAULT NULL
 ,`multiplexGroupNumber`  int(10)  NULL DEFAULT NULL
 ,`barcodeSequence`  varchar(20)  NULL DEFAULT NULL
 ,`meanLibSizeActual`  int(10)  NULL DEFAULT NULL
 ,`idOligoBarcodeB`  int(10)  NULL DEFAULT NULL
 ,`barcodeSequenceB`  varchar(20)  NULL DEFAULT NULL
 ,`qubitConcentration`  decimal(8,3)  NULL DEFAULT NULL
 ,`groupName`  varchar(200)  NULL DEFAULT NULL
 ,`qcCodeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`qcLibConcentration`  decimal(8,1)  NULL DEFAULT NULL
 ,`idLibPrepQCProtocol`  int(10)  NULL DEFAULT NULL
 ,`sampleVolume`  decimal(8,1)  NULL DEFAULT NULL
 ,`idLibPrepPerformedBy`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Sample 
--

INSERT INTO Sample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSample
  , number
  , name
  , description
  , concentration
  , codeConcentrationUnit
  , idSampleType
  , idOrganism
  , otherOrganism
  , idSampleSource
  , idSamplePrepMethod
  , otherSamplePrepMethod
  , idSeqLibProtocol
  , codeBioanalyzerChipType
  , idOligoBarcode
  , qualDate
  , qualFailed
  , qualBypassed
  , qual260nmTo280nmRatio
  , qual260nmTo230nmRatio
  , qualCalcConcentration
  , qual28sTo18sRibosomalRatio
  , qualRINNumber
  , idRequest
  , fragmentSizeFrom
  , fragmentSizeTo
  , seqPrepByCore
  , seqPrepDate
  , seqPrepFailed
  , seqPrepBypassed
  , qualFragmentSizeFrom
  , qualFragmentSizeTo
  , seqPrepLibConcentration
  , seqPrepQualCodeBioanalyzerChipType
  , seqPrepGelFragmentSizeFrom
  , seqPrepGelFragmentSizeTo
  , seqPrepStockLibVol
  , seqPrepStockEBVol
  , seqPrepStockDate
  , seqPrepStockFailed
  , seqPrepStockBypassed
  , prepInstructions
  , ccNumber
  , multiplexGroupNumber
  , barcodeSequence
  , meanLibSizeActual
  , idOligoBarcodeB
  , barcodeSequenceB
  , qubitConcentration
  , groupName
  , qcCodeApplication
  , qcLibConcentration
  , idLibPrepQCProtocol
  , sampleVolume
  , idLibPrepPerformedBy )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSample
  , number
  , name
  , description
  , concentration
  , codeConcentrationUnit
  , idSampleType
  , idOrganism
  , otherOrganism
  , idSampleSource
  , idSamplePrepMethod
  , otherSamplePrepMethod
  , idSeqLibProtocol
  , codeBioanalyzerChipType
  , idOligoBarcode
  , qualDate
  , qualFailed
  , qualBypassed
  , qual260nmTo280nmRatio
  , qual260nmTo230nmRatio
  , qualCalcConcentration
  , qual28sTo18sRibosomalRatio
  , qualRINNumber
  , idRequest
  , fragmentSizeFrom
  , fragmentSizeTo
  , seqPrepByCore
  , seqPrepDate
  , seqPrepFailed
  , seqPrepBypassed
  , qualFragmentSizeFrom
  , qualFragmentSizeTo
  , seqPrepLibConcentration
  , seqPrepQualCodeBioanalyzerChipType
  , seqPrepGelFragmentSizeFrom
  , seqPrepGelFragmentSizeTo
  , seqPrepStockLibVol
  , seqPrepStockEBVol
  , seqPrepStockDate
  , seqPrepStockFailed
  , seqPrepStockBypassed
  , prepInstructions
  , ccNumber
  , multiplexGroupNumber
  , barcodeSequence
  , meanLibSizeActual
  , idOligoBarcodeB
  , barcodeSequenceB
  , qubitConcentration
  , groupName
  , qcCodeApplication
  , qcLibConcentration
  , idLibPrepQCProtocol
  , sampleVolume
  , idLibPrepPerformedBy
  FROM Sample
  WHERE NOT EXISTS(SELECT * FROM Sample_Audit)
$$

--
-- Audit Triggers For Sample 
--


CREATE TRIGGER TrAI_Sample_FER AFTER INSERT ON Sample FOR EACH ROW
BEGIN
  INSERT INTO Sample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSample
  , number
  , name
  , description
  , concentration
  , codeConcentrationUnit
  , idSampleType
  , idOrganism
  , otherOrganism
  , idSampleSource
  , idSamplePrepMethod
  , otherSamplePrepMethod
  , idSeqLibProtocol
  , codeBioanalyzerChipType
  , idOligoBarcode
  , qualDate
  , qualFailed
  , qualBypassed
  , qual260nmTo280nmRatio
  , qual260nmTo230nmRatio
  , qualCalcConcentration
  , qual28sTo18sRibosomalRatio
  , qualRINNumber
  , idRequest
  , fragmentSizeFrom
  , fragmentSizeTo
  , seqPrepByCore
  , seqPrepDate
  , seqPrepFailed
  , seqPrepBypassed
  , qualFragmentSizeFrom
  , qualFragmentSizeTo
  , seqPrepLibConcentration
  , seqPrepQualCodeBioanalyzerChipType
  , seqPrepGelFragmentSizeFrom
  , seqPrepGelFragmentSizeTo
  , seqPrepStockLibVol
  , seqPrepStockEBVol
  , seqPrepStockDate
  , seqPrepStockFailed
  , seqPrepStockBypassed
  , prepInstructions
  , ccNumber
  , multiplexGroupNumber
  , barcodeSequence
  , meanLibSizeActual
  , idOligoBarcodeB
  , barcodeSequenceB
  , qubitConcentration
  , groupName
  , qcCodeApplication
  , qcLibConcentration
  , idLibPrepQCProtocol
  , sampleVolume
  , idLibPrepPerformedBy )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSample
  , NEW.number
  , NEW.name
  , NEW.description
  , NEW.concentration
  , NEW.codeConcentrationUnit
  , NEW.idSampleType
  , NEW.idOrganism
  , NEW.otherOrganism
  , NEW.idSampleSource
  , NEW.idSamplePrepMethod
  , NEW.otherSamplePrepMethod
  , NEW.idSeqLibProtocol
  , NEW.codeBioanalyzerChipType
  , NEW.idOligoBarcode
  , NEW.qualDate
  , NEW.qualFailed
  , NEW.qualBypassed
  , NEW.qual260nmTo280nmRatio
  , NEW.qual260nmTo230nmRatio
  , NEW.qualCalcConcentration
  , NEW.qual28sTo18sRibosomalRatio
  , NEW.qualRINNumber
  , NEW.idRequest
  , NEW.fragmentSizeFrom
  , NEW.fragmentSizeTo
  , NEW.seqPrepByCore
  , NEW.seqPrepDate
  , NEW.seqPrepFailed
  , NEW.seqPrepBypassed
  , NEW.qualFragmentSizeFrom
  , NEW.qualFragmentSizeTo
  , NEW.seqPrepLibConcentration
  , NEW.seqPrepQualCodeBioanalyzerChipType
  , NEW.seqPrepGelFragmentSizeFrom
  , NEW.seqPrepGelFragmentSizeTo
  , NEW.seqPrepStockLibVol
  , NEW.seqPrepStockEBVol
  , NEW.seqPrepStockDate
  , NEW.seqPrepStockFailed
  , NEW.seqPrepStockBypassed
  , NEW.prepInstructions
  , NEW.ccNumber
  , NEW.multiplexGroupNumber
  , NEW.barcodeSequence
  , NEW.meanLibSizeActual
  , NEW.idOligoBarcodeB
  , NEW.barcodeSequenceB
  , NEW.qubitConcentration
  , NEW.groupName
  , NEW.qcCodeApplication
  , NEW.qcLibConcentration
  , NEW.idLibPrepQCProtocol
  , NEW.sampleVolume
  , NEW.idLibPrepPerformedBy );
END;
$$


CREATE TRIGGER TrAU_Sample_FER AFTER UPDATE ON Sample FOR EACH ROW
BEGIN
  INSERT INTO Sample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSample
  , number
  , name
  , description
  , concentration
  , codeConcentrationUnit
  , idSampleType
  , idOrganism
  , otherOrganism
  , idSampleSource
  , idSamplePrepMethod
  , otherSamplePrepMethod
  , idSeqLibProtocol
  , codeBioanalyzerChipType
  , idOligoBarcode
  , qualDate
  , qualFailed
  , qualBypassed
  , qual260nmTo280nmRatio
  , qual260nmTo230nmRatio
  , qualCalcConcentration
  , qual28sTo18sRibosomalRatio
  , qualRINNumber
  , idRequest
  , fragmentSizeFrom
  , fragmentSizeTo
  , seqPrepByCore
  , seqPrepDate
  , seqPrepFailed
  , seqPrepBypassed
  , qualFragmentSizeFrom
  , qualFragmentSizeTo
  , seqPrepLibConcentration
  , seqPrepQualCodeBioanalyzerChipType
  , seqPrepGelFragmentSizeFrom
  , seqPrepGelFragmentSizeTo
  , seqPrepStockLibVol
  , seqPrepStockEBVol
  , seqPrepStockDate
  , seqPrepStockFailed
  , seqPrepStockBypassed
  , prepInstructions
  , ccNumber
  , multiplexGroupNumber
  , barcodeSequence
  , meanLibSizeActual
  , idOligoBarcodeB
  , barcodeSequenceB
  , qubitConcentration
  , groupName
  , qcCodeApplication
  , qcLibConcentration
  , idLibPrepQCProtocol
  , sampleVolume
  , idLibPrepPerformedBy )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSample
  , NEW.number
  , NEW.name
  , NEW.description
  , NEW.concentration
  , NEW.codeConcentrationUnit
  , NEW.idSampleType
  , NEW.idOrganism
  , NEW.otherOrganism
  , NEW.idSampleSource
  , NEW.idSamplePrepMethod
  , NEW.otherSamplePrepMethod
  , NEW.idSeqLibProtocol
  , NEW.codeBioanalyzerChipType
  , NEW.idOligoBarcode
  , NEW.qualDate
  , NEW.qualFailed
  , NEW.qualBypassed
  , NEW.qual260nmTo280nmRatio
  , NEW.qual260nmTo230nmRatio
  , NEW.qualCalcConcentration
  , NEW.qual28sTo18sRibosomalRatio
  , NEW.qualRINNumber
  , NEW.idRequest
  , NEW.fragmentSizeFrom
  , NEW.fragmentSizeTo
  , NEW.seqPrepByCore
  , NEW.seqPrepDate
  , NEW.seqPrepFailed
  , NEW.seqPrepBypassed
  , NEW.qualFragmentSizeFrom
  , NEW.qualFragmentSizeTo
  , NEW.seqPrepLibConcentration
  , NEW.seqPrepQualCodeBioanalyzerChipType
  , NEW.seqPrepGelFragmentSizeFrom
  , NEW.seqPrepGelFragmentSizeTo
  , NEW.seqPrepStockLibVol
  , NEW.seqPrepStockEBVol
  , NEW.seqPrepStockDate
  , NEW.seqPrepStockFailed
  , NEW.seqPrepStockBypassed
  , NEW.prepInstructions
  , NEW.ccNumber
  , NEW.multiplexGroupNumber
  , NEW.barcodeSequence
  , NEW.meanLibSizeActual
  , NEW.idOligoBarcodeB
  , NEW.barcodeSequenceB
  , NEW.qubitConcentration
  , NEW.groupName
  , NEW.qcCodeApplication
  , NEW.qcLibConcentration
  , NEW.idLibPrepQCProtocol
  , NEW.sampleVolume
  , NEW.idLibPrepPerformedBy );
END;
$$


CREATE TRIGGER TrAD_Sample_FER AFTER DELETE ON Sample FOR EACH ROW
BEGIN
  INSERT INTO Sample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSample
  , number
  , name
  , description
  , concentration
  , codeConcentrationUnit
  , idSampleType
  , idOrganism
  , otherOrganism
  , idSampleSource
  , idSamplePrepMethod
  , otherSamplePrepMethod
  , idSeqLibProtocol
  , codeBioanalyzerChipType
  , idOligoBarcode
  , qualDate
  , qualFailed
  , qualBypassed
  , qual260nmTo280nmRatio
  , qual260nmTo230nmRatio
  , qualCalcConcentration
  , qual28sTo18sRibosomalRatio
  , qualRINNumber
  , idRequest
  , fragmentSizeFrom
  , fragmentSizeTo
  , seqPrepByCore
  , seqPrepDate
  , seqPrepFailed
  , seqPrepBypassed
  , qualFragmentSizeFrom
  , qualFragmentSizeTo
  , seqPrepLibConcentration
  , seqPrepQualCodeBioanalyzerChipType
  , seqPrepGelFragmentSizeFrom
  , seqPrepGelFragmentSizeTo
  , seqPrepStockLibVol
  , seqPrepStockEBVol
  , seqPrepStockDate
  , seqPrepStockFailed
  , seqPrepStockBypassed
  , prepInstructions
  , ccNumber
  , multiplexGroupNumber
  , barcodeSequence
  , meanLibSizeActual
  , idOligoBarcodeB
  , barcodeSequenceB
  , qubitConcentration
  , groupName
  , qcCodeApplication
  , qcLibConcentration
  , idLibPrepQCProtocol
  , sampleVolume
  , idLibPrepPerformedBy )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSample
  , OLD.number
  , OLD.name
  , OLD.description
  , OLD.concentration
  , OLD.codeConcentrationUnit
  , OLD.idSampleType
  , OLD.idOrganism
  , OLD.otherOrganism
  , OLD.idSampleSource
  , OLD.idSamplePrepMethod
  , OLD.otherSamplePrepMethod
  , OLD.idSeqLibProtocol
  , OLD.codeBioanalyzerChipType
  , OLD.idOligoBarcode
  , OLD.qualDate
  , OLD.qualFailed
  , OLD.qualBypassed
  , OLD.qual260nmTo280nmRatio
  , OLD.qual260nmTo230nmRatio
  , OLD.qualCalcConcentration
  , OLD.qual28sTo18sRibosomalRatio
  , OLD.qualRINNumber
  , OLD.idRequest
  , OLD.fragmentSizeFrom
  , OLD.fragmentSizeTo
  , OLD.seqPrepByCore
  , OLD.seqPrepDate
  , OLD.seqPrepFailed
  , OLD.seqPrepBypassed
  , OLD.qualFragmentSizeFrom
  , OLD.qualFragmentSizeTo
  , OLD.seqPrepLibConcentration
  , OLD.seqPrepQualCodeBioanalyzerChipType
  , OLD.seqPrepGelFragmentSizeFrom
  , OLD.seqPrepGelFragmentSizeTo
  , OLD.seqPrepStockLibVol
  , OLD.seqPrepStockEBVol
  , OLD.seqPrepStockDate
  , OLD.seqPrepStockFailed
  , OLD.seqPrepStockBypassed
  , OLD.prepInstructions
  , OLD.ccNumber
  , OLD.multiplexGroupNumber
  , OLD.barcodeSequence
  , OLD.meanLibSizeActual
  , OLD.idOligoBarcodeB
  , OLD.barcodeSequenceB
  , OLD.qubitConcentration
  , OLD.groupName
  , OLD.qcCodeApplication
  , OLD.qcLibConcentration
  , OLD.idLibPrepQCProtocol
  , OLD.sampleVolume
  , OLD.idLibPrepPerformedBy );
END;
$$


--
-- Audit Table For ScanProtocol 
--

-- select 'Creating table ScanProtocol'$$

-- DROP TABLE IF EXISTS `ScanProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `ScanProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idScanProtocol`  int(10)  NULL DEFAULT NULL
 ,`scanProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for ScanProtocol 
--

INSERT INTO ScanProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive
  FROM ScanProtocol
  WHERE NOT EXISTS(SELECT * FROM ScanProtocol_Audit)
$$

--
-- Audit Triggers For ScanProtocol 
--


CREATE TRIGGER TrAI_ScanProtocol_FER AFTER INSERT ON ScanProtocol FOR EACH ROW
BEGIN
  INSERT INTO ScanProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idScanProtocol
  , NEW.scanProtocol
  , NEW.description
  , NEW.codeRequestCategory
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_ScanProtocol_FER AFTER UPDATE ON ScanProtocol FOR EACH ROW
BEGIN
  INSERT INTO ScanProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idScanProtocol
  , NEW.scanProtocol
  , NEW.description
  , NEW.codeRequestCategory
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_ScanProtocol_FER AFTER DELETE ON ScanProtocol FOR EACH ROW
BEGIN
  INSERT INTO ScanProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idScanProtocol
  , OLD.scanProtocol
  , OLD.description
  , OLD.codeRequestCategory
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For SealType 
--

-- select 'Creating table SealType'$$

-- DROP TABLE IF EXISTS `SealType_Audit`$$

CREATE TABLE IF NOT EXISTS `SealType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
 ,`sealType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SealType 
--

INSERT INTO SealType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSealType
  , sealType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeSealType
  , sealType
  , isActive
  FROM SealType
  WHERE NOT EXISTS(SELECT * FROM SealType_Audit)
$$

--
-- Audit Triggers For SealType 
--


CREATE TRIGGER TrAI_SealType_FER AFTER INSERT ON SealType FOR EACH ROW
BEGIN
  INSERT INTO SealType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeSealType
  , NEW.sealType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_SealType_FER AFTER UPDATE ON SealType FOR EACH ROW
BEGIN
  INSERT INTO SealType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeSealType
  , NEW.sealType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_SealType_FER AFTER DELETE ON SealType FOR EACH ROW
BEGIN
  INSERT INTO SealType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeSealType
  , OLD.sealType
  , OLD.isActive );
END;
$$


--
-- Audit Table For Segment 
--

-- select 'Creating table Segment'$$

-- DROP TABLE IF EXISTS `Segment_Audit`$$

CREATE TABLE IF NOT EXISTS `Segment_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSegment`  int(10)  NULL DEFAULT NULL
 ,`length`  int(10) unsigned  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10) unsigned  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Segment 
--

INSERT INTO Segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder
  FROM Segment
  WHERE NOT EXISTS(SELECT * FROM Segment_Audit)
$$

--
-- Audit Triggers For Segment 
--


CREATE TRIGGER TrAI_Segment_FER AFTER INSERT ON Segment FOR EACH ROW
BEGIN
  INSERT INTO Segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSegment
  , NEW.length
  , NEW.name
  , NEW.idGenomeBuild
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_Segment_FER AFTER UPDATE ON Segment FOR EACH ROW
BEGIN
  INSERT INTO Segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSegment
  , NEW.length
  , NEW.name
  , NEW.idGenomeBuild
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_Segment_FER AFTER DELETE ON Segment FOR EACH ROW
BEGIN
  INSERT INTO Segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSegment
  , OLD.length
  , OLD.name
  , OLD.idGenomeBuild
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For SeqLibProtocolApplication 
--

-- select 'Creating table SeqLibProtocolApplication'$$

-- DROP TABLE IF EXISTS `SeqLibProtocolApplication_Audit`$$

CREATE TABLE IF NOT EXISTS `SeqLibProtocolApplication_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SeqLibProtocolApplication 
--

INSERT INTO SeqLibProtocolApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , codeApplication )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSeqLibProtocol
  , codeApplication
  FROM SeqLibProtocolApplication
  WHERE NOT EXISTS(SELECT * FROM SeqLibProtocolApplication_Audit)
$$

--
-- Audit Triggers For SeqLibProtocolApplication 
--


CREATE TRIGGER TrAI_SeqLibProtocolApplication_FER AFTER INSERT ON SeqLibProtocolApplication FOR EACH ROW
BEGIN
  INSERT INTO SeqLibProtocolApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqLibProtocol
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAU_SeqLibProtocolApplication_FER AFTER UPDATE ON SeqLibProtocolApplication FOR EACH ROW
BEGIN
  INSERT INTO SeqLibProtocolApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqLibProtocol
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAD_SeqLibProtocolApplication_FER AFTER DELETE ON SeqLibProtocolApplication FOR EACH ROW
BEGIN
  INSERT INTO SeqLibProtocolApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSeqLibProtocol
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For SeqLibProtocol 
--

-- select 'Creating table SeqLibProtocol'$$

-- DROP TABLE IF EXISTS `SeqLibProtocol_Audit`$$

CREATE TABLE IF NOT EXISTS `SeqLibProtocol_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`seqLibProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`adapterSequenceRead1`  varchar(500)  NULL DEFAULT NULL
 ,`adapterSequenceRead2`  varchar(500)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SeqLibProtocol 
--

INSERT INTO SeqLibProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2 )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2
  FROM SeqLibProtocol
  WHERE NOT EXISTS(SELECT * FROM SeqLibProtocol_Audit)
$$

--
-- Audit Triggers For SeqLibProtocol 
--


CREATE TRIGGER TrAI_SeqLibProtocol_FER AFTER INSERT ON SeqLibProtocol FOR EACH ROW
BEGIN
  INSERT INTO SeqLibProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2 )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqLibProtocol
  , NEW.seqLibProtocol
  , NEW.description
  , NEW.url
  , NEW.isActive
  , NEW.adapterSequenceRead1
  , NEW.adapterSequenceRead2 );
END;
$$


CREATE TRIGGER TrAU_SeqLibProtocol_FER AFTER UPDATE ON SeqLibProtocol FOR EACH ROW
BEGIN
  INSERT INTO SeqLibProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2 )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqLibProtocol
  , NEW.seqLibProtocol
  , NEW.description
  , NEW.url
  , NEW.isActive
  , NEW.adapterSequenceRead1
  , NEW.adapterSequenceRead2 );
END;
$$


CREATE TRIGGER TrAD_SeqLibProtocol_FER AFTER DELETE ON SeqLibProtocol FOR EACH ROW
BEGIN
  INSERT INTO SeqLibProtocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2 )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSeqLibProtocol
  , OLD.seqLibProtocol
  , OLD.description
  , OLD.url
  , OLD.isActive
  , OLD.adapterSequenceRead1
  , OLD.adapterSequenceRead2 );
END;
$$


--
-- Audit Table For SeqLibTreatment 
--

-- select 'Creating table SeqLibTreatment'$$

-- DROP TABLE IF EXISTS `SeqLibTreatment_Audit`$$

CREATE TABLE IF NOT EXISTS `SeqLibTreatment_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
 ,`seqLibTreatment`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SeqLibTreatment 
--

INSERT INTO SeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive
  FROM SeqLibTreatment
  WHERE NOT EXISTS(SELECT * FROM SeqLibTreatment_Audit)
$$

--
-- Audit Triggers For SeqLibTreatment 
--


CREATE TRIGGER TrAI_SeqLibTreatment_FER AFTER INSERT ON SeqLibTreatment FOR EACH ROW
BEGIN
  INSERT INTO SeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqLibTreatment
  , NEW.seqLibTreatment
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_SeqLibTreatment_FER AFTER UPDATE ON SeqLibTreatment FOR EACH ROW
BEGIN
  INSERT INTO SeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqLibTreatment
  , NEW.seqLibTreatment
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_SeqLibTreatment_FER AFTER DELETE ON SeqLibTreatment FOR EACH ROW
BEGIN
  INSERT INTO SeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSeqLibTreatment
  , OLD.seqLibTreatment
  , OLD.isActive );
END;
$$


--
-- Audit Table For SeqRunType 
--

-- select 'Creating table SeqRunType'$$

-- DROP TABLE IF EXISTS `SeqRunType_Audit`$$

CREATE TABLE IF NOT EXISTS `SeqRunType_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`seqRunType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SeqRunType 
--

INSERT INTO SeqRunType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder
  FROM SeqRunType
  WHERE NOT EXISTS(SELECT * FROM SeqRunType_Audit)
$$

--
-- Audit Triggers For SeqRunType 
--


CREATE TRIGGER TrAI_SeqRunType_FER AFTER INSERT ON SeqRunType FOR EACH ROW
BEGIN
  INSERT INTO SeqRunType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqRunType
  , NEW.seqRunType
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_SeqRunType_FER AFTER UPDATE ON SeqRunType FOR EACH ROW
BEGIN
  INSERT INTO SeqRunType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSeqRunType
  , NEW.seqRunType
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_SeqRunType_FER AFTER DELETE ON SeqRunType FOR EACH ROW
BEGIN
  INSERT INTO SeqRunType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSeqRunType
  , OLD.seqRunType
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For SequenceLane 
--

-- select 'Creating table SequenceLane'$$

-- DROP TABLE IF EXISTS `SequenceLane_Audit`$$

CREATE TABLE IF NOT EXISTS `SequenceLane_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`analysisInstructions`  varchar(2000)  NULL DEFAULT NULL
 ,`idGenomeBuildAlignTo`  int(10)  NULL DEFAULT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`readCount`  int(10)  NULL DEFAULT NULL
 ,`pipelineVersion`  varchar(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SequenceLane 
--

INSERT INTO SequenceLane_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequenceLane
  , number
  , createDate
  , idRequest
  , idSample
  , idSeqRunType
  , idNumberSequencingCycles
  , analysisInstructions
  , idGenomeBuildAlignTo
  , idFlowCellChannel
  , readCount
  , pipelineVersion
  , idNumberSequencingCyclesAllowed )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSequenceLane
  , number
  , createDate
  , idRequest
  , idSample
  , idSeqRunType
  , idNumberSequencingCycles
  , analysisInstructions
  , idGenomeBuildAlignTo
  , idFlowCellChannel
  , readCount
  , pipelineVersion
  , idNumberSequencingCyclesAllowed
  FROM SequenceLane
  WHERE NOT EXISTS(SELECT * FROM SequenceLane_Audit)
$$

--
-- Audit Triggers For SequenceLane 
--


CREATE TRIGGER TrAI_SequenceLane_FER AFTER INSERT ON SequenceLane FOR EACH ROW
BEGIN
  INSERT INTO SequenceLane_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequenceLane
  , number
  , createDate
  , idRequest
  , idSample
  , idSeqRunType
  , idNumberSequencingCycles
  , analysisInstructions
  , idGenomeBuildAlignTo
  , idFlowCellChannel
  , readCount
  , pipelineVersion
  , idNumberSequencingCyclesAllowed )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSequenceLane
  , NEW.number
  , NEW.createDate
  , NEW.idRequest
  , NEW.idSample
  , NEW.idSeqRunType
  , NEW.idNumberSequencingCycles
  , NEW.analysisInstructions
  , NEW.idGenomeBuildAlignTo
  , NEW.idFlowCellChannel
  , NEW.readCount
  , NEW.pipelineVersion
  , NEW.idNumberSequencingCyclesAllowed );
END;
$$


CREATE TRIGGER TrAU_SequenceLane_FER AFTER UPDATE ON SequenceLane FOR EACH ROW
BEGIN
  INSERT INTO SequenceLane_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequenceLane
  , number
  , createDate
  , idRequest
  , idSample
  , idSeqRunType
  , idNumberSequencingCycles
  , analysisInstructions
  , idGenomeBuildAlignTo
  , idFlowCellChannel
  , readCount
  , pipelineVersion
  , idNumberSequencingCyclesAllowed )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSequenceLane
  , NEW.number
  , NEW.createDate
  , NEW.idRequest
  , NEW.idSample
  , NEW.idSeqRunType
  , NEW.idNumberSequencingCycles
  , NEW.analysisInstructions
  , NEW.idGenomeBuildAlignTo
  , NEW.idFlowCellChannel
  , NEW.readCount
  , NEW.pipelineVersion
  , NEW.idNumberSequencingCyclesAllowed );
END;
$$


CREATE TRIGGER TrAD_SequenceLane_FER AFTER DELETE ON SequenceLane FOR EACH ROW
BEGIN
  INSERT INTO SequenceLane_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequenceLane
  , number
  , createDate
  , idRequest
  , idSample
  , idSeqRunType
  , idNumberSequencingCycles
  , analysisInstructions
  , idGenomeBuildAlignTo
  , idFlowCellChannel
  , readCount
  , pipelineVersion
  , idNumberSequencingCyclesAllowed )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSequenceLane
  , OLD.number
  , OLD.createDate
  , OLD.idRequest
  , OLD.idSample
  , OLD.idSeqRunType
  , OLD.idNumberSequencingCycles
  , OLD.analysisInstructions
  , OLD.idGenomeBuildAlignTo
  , OLD.idFlowCellChannel
  , OLD.readCount
  , OLD.pipelineVersion
  , OLD.idNumberSequencingCyclesAllowed );
END;
$$


--
-- Audit Table For SequencingControl 
--

-- select 'Creating table SequencingControl'$$

-- DROP TABLE IF EXISTS `SequencingControl_Audit`$$

CREATE TABLE IF NOT EXISTS `SequencingControl_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSequencingControl`  int(10)  NULL DEFAULT NULL
 ,`sequencingControl`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SequencingControl 
--

INSERT INTO SequencingControl_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser
  FROM SequencingControl
  WHERE NOT EXISTS(SELECT * FROM SequencingControl_Audit)
$$

--
-- Audit Triggers For SequencingControl 
--


CREATE TRIGGER TrAI_SequencingControl_FER AFTER INSERT ON SequencingControl FOR EACH ROW
BEGIN
  INSERT INTO SequencingControl_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSequencingControl
  , NEW.sequencingControl
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_SequencingControl_FER AFTER UPDATE ON SequencingControl FOR EACH ROW
BEGIN
  INSERT INTO SequencingControl_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSequencingControl
  , NEW.sequencingControl
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_SequencingControl_FER AFTER DELETE ON SequencingControl FOR EACH ROW
BEGIN
  INSERT INTO SequencingControl_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSequencingControl
  , OLD.sequencingControl
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For SequencingPlatform 
--

-- select 'Creating table SequencingPlatform'$$

-- DROP TABLE IF EXISTS `SequencingPlatform_Audit`$$

CREATE TABLE IF NOT EXISTS `SequencingPlatform_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeSequencingPlatform`  varchar(10)  NULL DEFAULT NULL
 ,`sequencingPlatform`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SequencingPlatform 
--

INSERT INTO SequencingPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive
  FROM SequencingPlatform
  WHERE NOT EXISTS(SELECT * FROM SequencingPlatform_Audit)
$$

--
-- Audit Triggers For SequencingPlatform 
--


CREATE TRIGGER TrAI_SequencingPlatform_FER AFTER INSERT ON SequencingPlatform FOR EACH ROW
BEGIN
  INSERT INTO SequencingPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeSequencingPlatform
  , NEW.sequencingPlatform
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_SequencingPlatform_FER AFTER UPDATE ON SequencingPlatform FOR EACH ROW
BEGIN
  INSERT INTO SequencingPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeSequencingPlatform
  , NEW.sequencingPlatform
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_SequencingPlatform_FER AFTER DELETE ON SequencingPlatform FOR EACH ROW
BEGIN
  INSERT INTO SequencingPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeSequencingPlatform
  , OLD.sequencingPlatform
  , OLD.isActive );
END;
$$


--
-- Audit Table For SlideDesign 
--

-- select 'Creating table SlideDesign'$$

-- DROP TABLE IF EXISTS `SlideDesign_Audit`$$

CREATE TABLE IF NOT EXISTS `SlideDesign_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`slideDesignProtocolName`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`accessionNumberArrayExpress`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SlideDesign 
--

INSERT INTO SlideDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive
  FROM SlideDesign
  WHERE NOT EXISTS(SELECT * FROM SlideDesign_Audit)
$$

--
-- Audit Triggers For SlideDesign 
--


CREATE TRIGGER TrAI_SlideDesign_FER AFTER INSERT ON SlideDesign FOR EACH ROW
BEGIN
  INSERT INTO SlideDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSlideDesign
  , NEW.name
  , NEW.slideDesignProtocolName
  , NEW.idSlideProduct
  , NEW.accessionNumberArrayExpress
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_SlideDesign_FER AFTER UPDATE ON SlideDesign FOR EACH ROW
BEGIN
  INSERT INTO SlideDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSlideDesign
  , NEW.name
  , NEW.slideDesignProtocolName
  , NEW.idSlideProduct
  , NEW.accessionNumberArrayExpress
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_SlideDesign_FER AFTER DELETE ON SlideDesign FOR EACH ROW
BEGIN
  INSERT INTO SlideDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSlideDesign
  , OLD.name
  , OLD.slideDesignProtocolName
  , OLD.idSlideProduct
  , OLD.accessionNumberArrayExpress
  , OLD.isActive );
END;
$$


--
-- Audit Table For SlideProductApplication 
--

-- select 'Creating table SlideProductApplication'$$

-- DROP TABLE IF EXISTS `SlideProductApplication_Audit`$$

CREATE TABLE IF NOT EXISTS `SlideProductApplication_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SlideProductApplication 
--

INSERT INTO SlideProductApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , codeApplication )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSlideProduct
  , codeApplication
  FROM SlideProductApplication
  WHERE NOT EXISTS(SELECT * FROM SlideProductApplication_Audit)
$$

--
-- Audit Triggers For SlideProductApplication 
--


CREATE TRIGGER TrAI_SlideProductApplication_FER AFTER INSERT ON SlideProductApplication FOR EACH ROW
BEGIN
  INSERT INTO SlideProductApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSlideProduct
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAU_SlideProductApplication_FER AFTER UPDATE ON SlideProductApplication FOR EACH ROW
BEGIN
  INSERT INTO SlideProductApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSlideProduct
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAD_SlideProductApplication_FER AFTER DELETE ON SlideProductApplication FOR EACH ROW
BEGIN
  INSERT INTO SlideProductApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSlideProduct
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For SlideProduct 
--

-- select 'Creating table SlideProduct'$$

-- DROP TABLE IF EXISTS `SlideProduct_Audit`$$

CREATE TABLE IF NOT EXISTS `SlideProduct_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`arraysPerSlide`  int(10)  NULL DEFAULT NULL
 ,`slidesInSet`  int(10)  NULL DEFAULT NULL
 ,`isSlideSet`  char(1)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idBillingSlideProductClass`  int(10)  NULL DEFAULT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SlideProduct 
--

INSERT INTO SlideProduct_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , name
  , catalogNumber
  , isCustom
  , idLab
  , codeApplication
  , idVendor
  , idOrganism
  , arraysPerSlide
  , slidesInSet
  , isSlideSet
  , isActive
  , idBillingSlideProductClass
  , idBillingSlideServiceClass )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSlideProduct
  , name
  , catalogNumber
  , isCustom
  , idLab
  , codeApplication
  , idVendor
  , idOrganism
  , arraysPerSlide
  , slidesInSet
  , isSlideSet
  , isActive
  , idBillingSlideProductClass
  , idBillingSlideServiceClass
  FROM SlideProduct
  WHERE NOT EXISTS(SELECT * FROM SlideProduct_Audit)
$$

--
-- Audit Triggers For SlideProduct 
--


CREATE TRIGGER TrAI_SlideProduct_FER AFTER INSERT ON SlideProduct FOR EACH ROW
BEGIN
  INSERT INTO SlideProduct_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , name
  , catalogNumber
  , isCustom
  , idLab
  , codeApplication
  , idVendor
  , idOrganism
  , arraysPerSlide
  , slidesInSet
  , isSlideSet
  , isActive
  , idBillingSlideProductClass
  , idBillingSlideServiceClass )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSlideProduct
  , NEW.name
  , NEW.catalogNumber
  , NEW.isCustom
  , NEW.idLab
  , NEW.codeApplication
  , NEW.idVendor
  , NEW.idOrganism
  , NEW.arraysPerSlide
  , NEW.slidesInSet
  , NEW.isSlideSet
  , NEW.isActive
  , NEW.idBillingSlideProductClass
  , NEW.idBillingSlideServiceClass );
END;
$$


CREATE TRIGGER TrAU_SlideProduct_FER AFTER UPDATE ON SlideProduct FOR EACH ROW
BEGIN
  INSERT INTO SlideProduct_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , name
  , catalogNumber
  , isCustom
  , idLab
  , codeApplication
  , idVendor
  , idOrganism
  , arraysPerSlide
  , slidesInSet
  , isSlideSet
  , isActive
  , idBillingSlideProductClass
  , idBillingSlideServiceClass )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSlideProduct
  , NEW.name
  , NEW.catalogNumber
  , NEW.isCustom
  , NEW.idLab
  , NEW.codeApplication
  , NEW.idVendor
  , NEW.idOrganism
  , NEW.arraysPerSlide
  , NEW.slidesInSet
  , NEW.isSlideSet
  , NEW.isActive
  , NEW.idBillingSlideProductClass
  , NEW.idBillingSlideServiceClass );
END;
$$


CREATE TRIGGER TrAD_SlideProduct_FER AFTER DELETE ON SlideProduct FOR EACH ROW
BEGIN
  INSERT INTO SlideProduct_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlideProduct
  , name
  , catalogNumber
  , isCustom
  , idLab
  , codeApplication
  , idVendor
  , idOrganism
  , arraysPerSlide
  , slidesInSet
  , isSlideSet
  , isActive
  , idBillingSlideProductClass
  , idBillingSlideServiceClass )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSlideProduct
  , OLD.name
  , OLD.catalogNumber
  , OLD.isCustom
  , OLD.idLab
  , OLD.codeApplication
  , OLD.idVendor
  , OLD.idOrganism
  , OLD.arraysPerSlide
  , OLD.slidesInSet
  , OLD.isSlideSet
  , OLD.isActive
  , OLD.idBillingSlideProductClass
  , OLD.idBillingSlideServiceClass );
END;
$$


--
-- Audit Table For SlideSource 
--

-- select 'Creating table SlideSource'$$

-- DROP TABLE IF EXISTS `SlideSource_Audit`$$

CREATE TABLE IF NOT EXISTS `SlideSource_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeSlideSource`  varchar(10)  NULL DEFAULT NULL
 ,`slideSource`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SlideSource 
--

INSERT INTO SlideSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder
  FROM SlideSource
  WHERE NOT EXISTS(SELECT * FROM SlideSource_Audit)
$$

--
-- Audit Triggers For SlideSource 
--


CREATE TRIGGER TrAI_SlideSource_FER AFTER INSERT ON SlideSource FOR EACH ROW
BEGIN
  INSERT INTO SlideSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeSlideSource
  , NEW.slideSource
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_SlideSource_FER AFTER UPDATE ON SlideSource FOR EACH ROW
BEGIN
  INSERT INTO SlideSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeSlideSource
  , NEW.slideSource
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_SlideSource_FER AFTER DELETE ON SlideSource FOR EACH ROW
BEGIN
  INSERT INTO SlideSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeSlideSource
  , OLD.slideSource
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For Slide 
--

-- select 'Creating table Slide'$$

-- DROP TABLE IF EXISTS `Slide_Audit`$$

CREATE TABLE IF NOT EXISTS `Slide_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSlide`  int(10)  NULL DEFAULT NULL
 ,`barcode`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`slideName`  varchar(200)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Slide 
--

INSERT INTO Slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSlide
  , barcode
  , idSlideDesign
  , slideName
  FROM Slide
  WHERE NOT EXISTS(SELECT * FROM Slide_Audit)
$$

--
-- Audit Triggers For Slide 
--


CREATE TRIGGER TrAI_Slide_FER AFTER INSERT ON Slide FOR EACH ROW
BEGIN
  INSERT INTO Slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSlide
  , NEW.barcode
  , NEW.idSlideDesign
  , NEW.slideName );
END;
$$


CREATE TRIGGER TrAU_Slide_FER AFTER UPDATE ON Slide FOR EACH ROW
BEGIN
  INSERT INTO Slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSlide
  , NEW.barcode
  , NEW.idSlideDesign
  , NEW.slideName );
END;
$$


CREATE TRIGGER TrAD_Slide_FER AFTER DELETE ON Slide FOR EACH ROW
BEGIN
  INSERT INTO Slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSlide
  , OLD.barcode
  , OLD.idSlideDesign
  , OLD.slideName );
END;
$$


--
-- Audit Table For State 
--

-- select 'Creating table State'$$

-- DROP TABLE IF EXISTS `State_Audit`$$

CREATE TABLE IF NOT EXISTS `State_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeState`  varchar(10)  NULL DEFAULT NULL
 ,`state`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for State 
--

INSERT INTO State_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeState
  , state
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeState
  , state
  , isActive
  FROM State
  WHERE NOT EXISTS(SELECT * FROM State_Audit)
$$

--
-- Audit Triggers For State 
--


CREATE TRIGGER TrAI_State_FER AFTER INSERT ON State FOR EACH ROW
BEGIN
  INSERT INTO State_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeState
  , state
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeState
  , NEW.state
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_State_FER AFTER UPDATE ON State FOR EACH ROW
BEGIN
  INSERT INTO State_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeState
  , state
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeState
  , NEW.state
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_State_FER AFTER DELETE ON State FOR EACH ROW
BEGIN
  INSERT INTO State_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeState
  , state
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeState
  , OLD.state
  , OLD.isActive );
END;
$$


--
-- Audit Table For Step 
--

-- select 'Creating table Step'$$

-- DROP TABLE IF EXISTS `Step_Audit`$$

CREATE TABLE IF NOT EXISTS `Step_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
 ,`step`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Step 
--

INSERT INTO Step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeStep
  , step
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeStep
  , step
  , isActive
  , sortOrder
  FROM Step
  WHERE NOT EXISTS(SELECT * FROM Step_Audit)
$$

--
-- Audit Triggers For Step 
--


CREATE TRIGGER TrAI_Step_FER AFTER INSERT ON Step FOR EACH ROW
BEGIN
  INSERT INTO Step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeStep
  , NEW.step
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_Step_FER AFTER UPDATE ON Step FOR EACH ROW
BEGIN
  INSERT INTO Step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeStep
  , NEW.step
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_Step_FER AFTER DELETE ON Step FOR EACH ROW
BEGIN
  INSERT INTO Step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeStep
  , OLD.step
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For SubmissionInstruction 
--

-- select 'Creating table SubmissionInstruction'$$

-- DROP TABLE IF EXISTS `SubmissionInstruction_Audit`$$

CREATE TABLE IF NOT EXISTS `SubmissionInstruction_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idSubmissionInstruction`  int(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`url`  varchar(2000)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for SubmissionInstruction 
--

INSERT INTO SubmissionInstruction_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass
  FROM SubmissionInstruction
  WHERE NOT EXISTS(SELECT * FROM SubmissionInstruction_Audit)
$$

--
-- Audit Triggers For SubmissionInstruction 
--


CREATE TRIGGER TrAI_SubmissionInstruction_FER AFTER INSERT ON SubmissionInstruction FOR EACH ROW
BEGIN
  INSERT INTO SubmissionInstruction_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idSubmissionInstruction
  , NEW.description
  , NEW.url
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.codeBioanalyzerChipType
  , NEW.idBillingSlideServiceClass );
END;
$$


CREATE TRIGGER TrAU_SubmissionInstruction_FER AFTER UPDATE ON SubmissionInstruction FOR EACH ROW
BEGIN
  INSERT INTO SubmissionInstruction_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idSubmissionInstruction
  , NEW.description
  , NEW.url
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.codeBioanalyzerChipType
  , NEW.idBillingSlideServiceClass );
END;
$$


CREATE TRIGGER TrAD_SubmissionInstruction_FER AFTER DELETE ON SubmissionInstruction FOR EACH ROW
BEGIN
  INSERT INTO SubmissionInstruction_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idSubmissionInstruction
  , OLD.description
  , OLD.url
  , OLD.codeRequestCategory
  , OLD.codeApplication
  , OLD.codeBioanalyzerChipType
  , OLD.idBillingSlideServiceClass );
END;
$$


--
-- Audit Table For Topic 
--

-- select 'Creating table Topic'$$

-- DROP TABLE IF EXISTS `Topic_Audit`$$

CREATE TABLE IF NOT EXISTS `Topic_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`idParentTopic`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Topic 
--

INSERT INTO Topic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , name
  , description
  , idParentTopic
  , idLab
  , createdBy
  , createDate
  , idAppUser
  , codeVisibility
  , idInstitution )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idTopic
  , name
  , description
  , idParentTopic
  , idLab
  , createdBy
  , createDate
  , idAppUser
  , codeVisibility
  , idInstitution
  FROM Topic
  WHERE NOT EXISTS(SELECT * FROM Topic_Audit)
$$

--
-- Audit Triggers For Topic 
--


CREATE TRIGGER TrAI_Topic_FER AFTER INSERT ON Topic FOR EACH ROW
BEGIN
  INSERT INTO Topic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , name
  , description
  , idParentTopic
  , idLab
  , createdBy
  , createDate
  , idAppUser
  , codeVisibility
  , idInstitution )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.name
  , NEW.description
  , NEW.idParentTopic
  , NEW.idLab
  , NEW.createdBy
  , NEW.createDate
  , NEW.idAppUser
  , NEW.codeVisibility
  , NEW.idInstitution );
END;
$$


CREATE TRIGGER TrAU_Topic_FER AFTER UPDATE ON Topic FOR EACH ROW
BEGIN
  INSERT INTO Topic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , name
  , description
  , idParentTopic
  , idLab
  , createdBy
  , createDate
  , idAppUser
  , codeVisibility
  , idInstitution )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idTopic
  , NEW.name
  , NEW.description
  , NEW.idParentTopic
  , NEW.idLab
  , NEW.createdBy
  , NEW.createDate
  , NEW.idAppUser
  , NEW.codeVisibility
  , NEW.idInstitution );
END;
$$


CREATE TRIGGER TrAD_Topic_FER AFTER DELETE ON Topic FOR EACH ROW
BEGIN
  INSERT INTO Topic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTopic
  , name
  , description
  , idParentTopic
  , idLab
  , createdBy
  , createDate
  , idAppUser
  , codeVisibility
  , idInstitution )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idTopic
  , OLD.name
  , OLD.description
  , OLD.idParentTopic
  , OLD.idLab
  , OLD.createdBy
  , OLD.createDate
  , OLD.idAppUser
  , OLD.codeVisibility
  , OLD.idInstitution );
END;
$$


--
-- Audit Table For TreatmentEntry 
--

-- select 'Creating table TreatmentEntry'$$

-- DROP TABLE IF EXISTS `TreatmentEntry_Audit`$$

CREATE TABLE IF NOT EXISTS `TreatmentEntry_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idTreatmentEntry`  int(10)  NULL DEFAULT NULL
 ,`treatment`  varchar(2000)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for TreatmentEntry 
--

INSERT INTO TreatmentEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel
  FROM TreatmentEntry
  WHERE NOT EXISTS(SELECT * FROM TreatmentEntry_Audit)
$$

--
-- Audit Triggers For TreatmentEntry 
--


CREATE TRIGGER TrAI_TreatmentEntry_FER AFTER INSERT ON TreatmentEntry FOR EACH ROW
BEGIN
  INSERT INTO TreatmentEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idTreatmentEntry
  , NEW.treatment
  , NEW.idSample
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_TreatmentEntry_FER AFTER UPDATE ON TreatmentEntry FOR EACH ROW
BEGIN
  INSERT INTO TreatmentEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idTreatmentEntry
  , NEW.treatment
  , NEW.idSample
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_TreatmentEntry_FER AFTER DELETE ON TreatmentEntry FOR EACH ROW
BEGIN
  INSERT INTO TreatmentEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idTreatmentEntry
  , OLD.treatment
  , OLD.idSample
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For UnloadDataTrack 
--

-- select 'Creating table UnloadDataTrack'$$

-- DROP TABLE IF EXISTS `UnloadDataTrack_Audit`$$

CREATE TABLE IF NOT EXISTS `UnloadDataTrack_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idUnloadDataTrack`  int(10)  NULL DEFAULT NULL
 ,`typeName`  varchar(2000)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for UnloadDataTrack 
--

INSERT INTO UnloadDataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild
  FROM UnloadDataTrack
  WHERE NOT EXISTS(SELECT * FROM UnloadDataTrack_Audit)
$$

--
-- Audit Triggers For UnloadDataTrack 
--


CREATE TRIGGER TrAI_UnloadDataTrack_FER AFTER INSERT ON UnloadDataTrack FOR EACH ROW
BEGIN
  INSERT INTO UnloadDataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idUnloadDataTrack
  , NEW.typeName
  , NEW.idAppUser
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAU_UnloadDataTrack_FER AFTER UPDATE ON UnloadDataTrack FOR EACH ROW
BEGIN
  INSERT INTO UnloadDataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idUnloadDataTrack
  , NEW.typeName
  , NEW.idAppUser
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAD_UnloadDataTrack_FER AFTER DELETE ON UnloadDataTrack FOR EACH ROW
BEGIN
  INSERT INTO UnloadDataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idUnloadDataTrack
  , OLD.typeName
  , OLD.idAppUser
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For UserPermissionKind 
--

-- select 'Creating table UserPermissionKind'$$

-- DROP TABLE IF EXISTS `UserPermissionKind_Audit`$$

CREATE TABLE IF NOT EXISTS `UserPermissionKind_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeUserPermissionKind`  varchar(10)  NULL DEFAULT NULL
 ,`userPermissionKind`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for UserPermissionKind 
--

INSERT INTO UserPermissionKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeUserPermissionKind
  , userPermissionKind
  , isActive
  FROM UserPermissionKind
  WHERE NOT EXISTS(SELECT * FROM UserPermissionKind_Audit)
$$

--
-- Audit Triggers For UserPermissionKind 
--


CREATE TRIGGER TrAI_UserPermissionKind_FER AFTER INSERT ON UserPermissionKind FOR EACH ROW
BEGIN
  INSERT INTO UserPermissionKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeUserPermissionKind
  , NEW.userPermissionKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_UserPermissionKind_FER AFTER UPDATE ON UserPermissionKind FOR EACH ROW
BEGIN
  INSERT INTO UserPermissionKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeUserPermissionKind
  , NEW.userPermissionKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_UserPermissionKind_FER AFTER DELETE ON UserPermissionKind FOR EACH ROW
BEGIN
  INSERT INTO UserPermissionKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeUserPermissionKind
  , OLD.userPermissionKind
  , OLD.isActive );
END;
$$


--
-- Audit Table For Vendor 
--

-- select 'Creating table Vendor'$$

-- DROP TABLE IF EXISTS `Vendor_Audit`$$

CREATE TABLE IF NOT EXISTS `Vendor_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`vendorName`  varchar(100)  NULL DEFAULT NULL
 ,`description`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Vendor 
--

INSERT INTO Vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idVendor
  , vendorName
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idVendor
  , vendorName
  , description
  , isActive
  FROM Vendor
  WHERE NOT EXISTS(SELECT * FROM Vendor_Audit)
$$

--
-- Audit Triggers For Vendor 
--


CREATE TRIGGER TrAI_Vendor_FER AFTER INSERT ON Vendor FOR EACH ROW
BEGIN
  INSERT INTO Vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idVendor
  , NEW.vendorName
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Vendor_FER AFTER UPDATE ON Vendor FOR EACH ROW
BEGIN
  INSERT INTO Vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idVendor
  , NEW.vendorName
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Vendor_FER AFTER DELETE ON Vendor FOR EACH ROW
BEGIN
  INSERT INTO Vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idVendor
  , OLD.vendorName
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For Visibility 
--

-- select 'Creating table Visibility'$$

-- DROP TABLE IF EXISTS `Visibility_Audit`$$

CREATE TABLE IF NOT EXISTS `Visibility_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`visibility`  varchar(100)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Visibility 
--

INSERT INTO Visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeVisibility
  , visibility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , codeVisibility
  , visibility
  FROM Visibility
  WHERE NOT EXISTS(SELECT * FROM Visibility_Audit)
$$

--
-- Audit Triggers For Visibility 
--


CREATE TRIGGER TrAI_Visibility_FER AFTER INSERT ON Visibility FOR EACH ROW
BEGIN
  INSERT INTO Visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeVisibility
  , visibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.codeVisibility
  , NEW.visibility );
END;
$$


CREATE TRIGGER TrAU_Visibility_FER AFTER UPDATE ON Visibility FOR EACH ROW
BEGIN
  INSERT INTO Visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeVisibility
  , visibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.codeVisibility
  , NEW.visibility );
END;
$$


CREATE TRIGGER TrAD_Visibility_FER AFTER DELETE ON Visibility FOR EACH ROW
BEGIN
  INSERT INTO Visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , codeVisibility
  , visibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.codeVisibility
  , OLD.visibility );
END;
$$


--
-- Audit Table For Workitem 
--

-- select 'Creating table Workitem'$$

-- DROP TABLE IF EXISTS `Workitem_Audit`$$

CREATE TABLE IF NOT EXISTS `Workitem_Audit` (
  `AuditAppuser`       varchar(128) NULL
 ,`AuditOperation`     char(1)      NULL
 ,`AuditSystemUser`    varchar(30)  NULL
 ,`AuditOperationDate` datetime     NULL
 ,`AuditEditedByPersonID` int(10)   NULL
 ,`idWorkItem`  int(10)  NULL DEFAULT NULL
 ,`codeStepNext`  varchar(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSample`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`status`  varchar(50)  NULL DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=latin1
$$


--
-- Initial audit table rows for Workitem 
--

INSERT INTO Workitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idWorkItem
  , codeStepNext
  , idSample
  , idLabeledSample
  , idHybridization
  , idRequest
  , createDate
  , idSequenceLane
  , idFlowCellChannel
  , idCoreFacility
  , status )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , 0
  , idWorkItem
  , codeStepNext
  , idSample
  , idLabeledSample
  , idHybridization
  , idRequest
  , createDate
  , idSequenceLane
  , idFlowCellChannel
  , idCoreFacility
  , status
  FROM Workitem
  WHERE NOT EXISTS(SELECT * FROM Workitem_Audit)
$$

--
-- Audit Triggers For Workitem 
--


CREATE TRIGGER TrAI_Workitem_FER AFTER INSERT ON Workitem FOR EACH ROW
BEGIN
  INSERT INTO Workitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idWorkItem
  , codeStepNext
  , idSample
  , idLabeledSample
  , idHybridization
  , idRequest
  , createDate
  , idSequenceLane
  , idFlowCellChannel
  , idCoreFacility
  , status )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , 0
  , NEW.idWorkItem
  , NEW.codeStepNext
  , NEW.idSample
  , NEW.idLabeledSample
  , NEW.idHybridization
  , NEW.idRequest
  , NEW.createDate
  , NEW.idSequenceLane
  , NEW.idFlowCellChannel
  , NEW.idCoreFacility
  , NEW.status );
END;
$$


CREATE TRIGGER TrAU_Workitem_FER AFTER UPDATE ON Workitem FOR EACH ROW
BEGIN
  INSERT INTO Workitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idWorkItem
  , codeStepNext
  , idSample
  , idLabeledSample
  , idHybridization
  , idRequest
  , createDate
  , idSequenceLane
  , idFlowCellChannel
  , idCoreFacility
  , status )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , 0
  , NEW.idWorkItem
  , NEW.codeStepNext
  , NEW.idSample
  , NEW.idLabeledSample
  , NEW.idHybridization
  , NEW.idRequest
  , NEW.createDate
  , NEW.idSequenceLane
  , NEW.idFlowCellChannel
  , NEW.idCoreFacility
  , NEW.status );
END;
$$


CREATE TRIGGER TrAD_Workitem_FER AFTER DELETE ON Workitem FOR EACH ROW
BEGIN
  INSERT INTO Workitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , AuditEditedByPersonID
  , idWorkItem
  , codeStepNext
  , idSample
  , idLabeledSample
  , idHybridization
  , idRequest
  , createDate
  , idSequenceLane
  , idFlowCellChannel
  , idCoreFacility
  , status )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , 0
  , OLD.idWorkItem
  , OLD.codeStepNext
  , OLD.idSample
  , OLD.idLabeledSample
  , OLD.idHybridization
  , OLD.idRequest
  , OLD.createDate
  , OLD.idSequenceLane
  , OLD.idFlowCellChannel
  , OLD.idCoreFacility
  , OLD.status );
END;
$$
