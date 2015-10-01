
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
DROP TRIGGER IF EXISTS TrAI_IScanChip_FER
$$
DROP TRIGGER IF EXISTS TrAU_IScanChip_FER
$$
DROP TRIGGER IF EXISTS TrAD_IScanChip_FER
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
DROP TRIGGER IF EXISTS TrAI_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAU_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAD_MetrixObject_FER
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
DROP TRIGGER IF EXISTS TrAI_VisitLog_FER
$$
DROP TRIGGER IF EXISTS TrAU_VisitLog_FER
$$
DROP TRIGGER IF EXISTS TrAD_VisitLog_FER
$$
DROP TRIGGER IF EXISTS TrAI_WorkItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_WorkItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_WorkItem_FER
$$


--
-- Audit Table For alignmentplatform 
--

CREATE TABLE IF NOT EXISTS `alignmentplatform_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`alignmentPlatformName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For alignmentplatform 
--


CREATE TRIGGER TrAI_alignmentplatform_FER AFTER INSERT ON alignmentplatform FOR EACH ROW
BEGIN
  INSERT INTO alignmentplatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAlignmentPlatform
  , NEW.alignmentPlatformName
  , NEW.webServiceName
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_alignmentplatform_FER AFTER UPDATE ON alignmentplatform FOR EACH ROW
BEGIN
  INSERT INTO alignmentplatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAlignmentPlatform
  , NEW.alignmentPlatformName
  , NEW.webServiceName
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_alignmentplatform_FER AFTER DELETE ON alignmentplatform FOR EACH ROW
BEGIN
  INSERT INTO alignmentplatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAlignmentPlatform
  , OLD.alignmentPlatformName
  , OLD.webServiceName
  , OLD.isActive );
END;
$$


--
-- Audit Table For alignmentprofilegenomeindex 
--

CREATE TABLE IF NOT EXISTS `alignmentprofilegenomeindex_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For alignmentprofilegenomeindex 
--


CREATE TRIGGER TrAI_alignmentprofilegenomeindex_FER AFTER INSERT ON alignmentprofilegenomeindex FOR EACH ROW
BEGIN
  INSERT INTO alignmentprofilegenomeindex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAlignmentProfile
  , NEW.idGenomeIndex );
END;
$$


CREATE TRIGGER TrAU_alignmentprofilegenomeindex_FER AFTER UPDATE ON alignmentprofilegenomeindex FOR EACH ROW
BEGIN
  INSERT INTO alignmentprofilegenomeindex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAlignmentProfile
  , NEW.idGenomeIndex );
END;
$$


CREATE TRIGGER TrAD_alignmentprofilegenomeindex_FER AFTER DELETE ON alignmentprofilegenomeindex FOR EACH ROW
BEGIN
  INSERT INTO alignmentprofilegenomeindex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAlignmentProfile
  , OLD.idGenomeIndex );
END;
$$


--
-- Audit Table For alignmentprofile 
--

CREATE TABLE IF NOT EXISTS `alignmentprofile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`alignmentProfileName`  varchar(120)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`parameters`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For alignmentprofile 
--


CREATE TRIGGER TrAI_alignmentprofile_FER AFTER INSERT ON alignmentprofile FOR EACH ROW
BEGIN
  INSERT INTO alignmentprofile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAlignmentProfile
  , NEW.alignmentProfileName
  , NEW.description
  , NEW.parameters
  , NEW.isActive
  , NEW.idAlignmentPlatform
  , NEW.idSeqRunType );
END;
$$


CREATE TRIGGER TrAU_alignmentprofile_FER AFTER UPDATE ON alignmentprofile FOR EACH ROW
BEGIN
  INSERT INTO alignmentprofile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAlignmentProfile
  , NEW.alignmentProfileName
  , NEW.description
  , NEW.parameters
  , NEW.isActive
  , NEW.idAlignmentPlatform
  , NEW.idSeqRunType );
END;
$$


CREATE TRIGGER TrAD_alignmentprofile_FER AFTER DELETE ON alignmentprofile FOR EACH ROW
BEGIN
  INSERT INTO alignmentprofile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentProfile
  , alignmentProfileName
  , description
  , parameters
  , isActive
  , idAlignmentPlatform
  , idSeqRunType )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For analysiscollaborator 
--

CREATE TABLE IF NOT EXISTS `analysiscollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysiscollaborator 
--


CREATE TRIGGER TrAI_analysiscollaborator_FER AFTER INSERT ON analysiscollaborator FOR EACH ROW
BEGIN
  INSERT INTO analysiscollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysis
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAU_analysiscollaborator_FER AFTER UPDATE ON analysiscollaborator FOR EACH ROW
BEGIN
  INSERT INTO analysiscollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysis
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAD_analysiscollaborator_FER AFTER DELETE ON analysiscollaborator FOR EACH ROW
BEGIN
  INSERT INTO analysiscollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAnalysis
  , OLD.idAppUser
  , OLD.canUploadData
  , OLD.canUpdate );
END;
$$


--
-- Audit Table For analysisexperimentitem 
--

CREATE TABLE IF NOT EXISTS `analysisexperimentitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisExperimentItem`  int(10)  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysisexperimentitem 
--


CREATE TRIGGER TrAI_analysisexperimentitem_FER AFTER INSERT ON analysisexperimentitem FOR EACH ROW
BEGIN
  INSERT INTO analysisexperimentitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisExperimentItem
  , NEW.idSequenceLane
  , NEW.idHybridization
  , NEW.comments
  , NEW.idAnalysis
  , NEW.idRequest
  , NEW.idSample );
END;
$$


CREATE TRIGGER TrAU_analysisexperimentitem_FER AFTER UPDATE ON analysisexperimentitem FOR EACH ROW
BEGIN
  INSERT INTO analysisexperimentitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisExperimentItem
  , NEW.idSequenceLane
  , NEW.idHybridization
  , NEW.comments
  , NEW.idAnalysis
  , NEW.idRequest
  , NEW.idSample );
END;
$$


CREATE TRIGGER TrAD_analysisexperimentitem_FER AFTER DELETE ON analysisexperimentitem FOR EACH ROW
BEGIN
  INSERT INTO analysisexperimentitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
  , idSample )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For analysisfile 
--

CREATE TABLE IF NOT EXISTS `analysisfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`uploadDate`  datetime  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`baseFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysisfile 
--


CREATE TRIGGER TrAI_analysisfile_FER AFTER INSERT ON analysisfile FOR EACH ROW
BEGIN
  INSERT INTO analysisfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_analysisfile_FER AFTER UPDATE ON analysisfile FOR EACH ROW
BEGIN
  INSERT INTO analysisfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_analysisfile_FER AFTER DELETE ON analysisfile FOR EACH ROW
BEGIN
  INSERT INTO analysisfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For analysisgenomebuild 
--

CREATE TABLE IF NOT EXISTS `analysisgenomebuild_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysisgenomebuild 
--


CREATE TRIGGER TrAI_analysisgenomebuild_FER AFTER INSERT ON analysisgenomebuild FOR EACH ROW
BEGIN
  INSERT INTO analysisgenomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysis
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAU_analysisgenomebuild_FER AFTER UPDATE ON analysisgenomebuild FOR EACH ROW
BEGIN
  INSERT INTO analysisgenomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysis
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAD_analysisgenomebuild_FER AFTER DELETE ON analysisgenomebuild FOR EACH ROW
BEGIN
  INSERT INTO analysisgenomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAnalysis
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For analysisgroupitem 
--

CREATE TABLE IF NOT EXISTS `analysisgroupitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysisgroupitem 
--


CREATE TRIGGER TrAI_analysisgroupitem_FER AFTER INSERT ON analysisgroupitem FOR EACH ROW
BEGIN
  INSERT INTO analysisgroupitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisGroup
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAU_analysisgroupitem_FER AFTER UPDATE ON analysisgroupitem FOR EACH ROW
BEGIN
  INSERT INTO analysisgroupitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisGroup
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAD_analysisgroupitem_FER AFTER DELETE ON analysisgroupitem FOR EACH ROW
BEGIN
  INSERT INTO analysisgroupitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAnalysisGroup
  , OLD.idAnalysis );
END;
$$


--
-- Audit Table For analysisgroup 
--

CREATE TABLE IF NOT EXISTS `analysisgroup_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysisgroup 
--


CREATE TRIGGER TrAI_analysisgroup_FER AFTER INSERT ON analysisgroup FOR EACH ROW
BEGIN
  INSERT INTO analysisgroup_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisGroup
  , NEW.name
  , NEW.description
  , NEW.idLab
  , NEW.codeVisibility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_analysisgroup_FER AFTER UPDATE ON analysisgroup FOR EACH ROW
BEGIN
  INSERT INTO analysisgroup_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisGroup
  , NEW.name
  , NEW.description
  , NEW.idLab
  , NEW.codeVisibility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_analysisgroup_FER AFTER DELETE ON analysisgroup FOR EACH ROW
BEGIN
  INSERT INTO analysisgroup_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , name
  , description
  , idLab
  , codeVisibility
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAnalysisGroup
  , OLD.name
  , OLD.description
  , OLD.idLab
  , OLD.codeVisibility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For analysisprotocol 
--

CREATE TABLE IF NOT EXISTS `analysisprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisProtocol`  int(10)  NULL DEFAULT NULL
 ,`analysisProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysisprotocol 
--


CREATE TRIGGER TrAI_analysisprotocol_FER AFTER INSERT ON analysisprotocol FOR EACH ROW
BEGIN
  INSERT INTO analysisprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisProtocol
  , NEW.analysisProtocol
  , NEW.description
  , NEW.url
  , NEW.idAnalysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_analysisprotocol_FER AFTER UPDATE ON analysisprotocol FOR EACH ROW
BEGIN
  INSERT INTO analysisprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisProtocol
  , NEW.analysisProtocol
  , NEW.description
  , NEW.url
  , NEW.idAnalysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_analysisprotocol_FER AFTER DELETE ON analysisprotocol FOR EACH ROW
BEGIN
  INSERT INTO analysisprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisProtocol
  , analysisProtocol
  , description
  , url
  , idAnalysisType
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For analysistotopic 
--

CREATE TABLE IF NOT EXISTS `analysistotopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysistotopic 
--


CREATE TRIGGER TrAI_analysistotopic_FER AFTER INSERT ON analysistotopic FOR EACH ROW
BEGIN
  INSERT INTO analysistotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idAnalysis )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idTopic
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAU_analysistotopic_FER AFTER UPDATE ON analysistotopic FOR EACH ROW
BEGIN
  INSERT INTO analysistotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idAnalysis )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idTopic
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAD_analysistotopic_FER AFTER DELETE ON analysistotopic FOR EACH ROW
BEGIN
  INSERT INTO analysistotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idAnalysis )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idTopic
  , OLD.idAnalysis );
END;
$$


--
-- Audit Table For analysistype 
--

CREATE TABLE IF NOT EXISTS `analysistype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`analysisType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysistype 
--


CREATE TRIGGER TrAI_analysistype_FER AFTER INSERT ON analysistype FOR EACH ROW
BEGIN
  INSERT INTO analysistype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisType
  , NEW.analysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_analysistype_FER AFTER UPDATE ON analysistype FOR EACH ROW
BEGIN
  INSERT INTO analysistype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnalysisType
  , NEW.analysisType
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_analysistype_FER AFTER DELETE ON analysistype FOR EACH ROW
BEGIN
  INSERT INTO analysistype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAnalysisType
  , OLD.analysisType
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For analysis 
--

CREATE TABLE IF NOT EXISTS `analysis_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For analysis 
--


CREATE TRIGGER TrAI_analysis_FER AFTER INSERT ON analysis FOR EACH ROW
BEGIN
  INSERT INTO analysis_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_analysis_FER AFTER UPDATE ON analysis FOR EACH ROW
BEGIN
  INSERT INTO analysis_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_analysis_FER AFTER DELETE ON analysis FOR EACH ROW
BEGIN
  INSERT INTO analysis_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For annotationreportfield 
--

CREATE TABLE IF NOT EXISTS `annotationreportfield_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnnotationReportField`  int(10)  NULL DEFAULT NULL
 ,`source`  varchar(50)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`display`  varchar(50)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`dictionaryLookUpTable`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For annotationreportfield 
--


CREATE TRIGGER TrAI_annotationreportfield_FER AFTER INSERT ON annotationreportfield FOR EACH ROW
BEGIN
  INSERT INTO annotationreportfield_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnnotationReportField
  , NEW.source
  , NEW.fieldName
  , NEW.display
  , NEW.isCustom
  , NEW.sortOrder
  , NEW.dictionaryLookUpTable );
END;
$$


CREATE TRIGGER TrAU_annotationreportfield_FER AFTER UPDATE ON annotationreportfield FOR EACH ROW
BEGIN
  INSERT INTO annotationreportfield_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAnnotationReportField
  , NEW.source
  , NEW.fieldName
  , NEW.display
  , NEW.isCustom
  , NEW.sortOrder
  , NEW.dictionaryLookUpTable );
END;
$$


CREATE TRIGGER TrAD_annotationreportfield_FER AFTER DELETE ON annotationreportfield FOR EACH ROW
BEGIN
  INSERT INTO annotationreportfield_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnnotationReportField
  , source
  , fieldName
  , display
  , isCustom
  , sortOrder
  , dictionaryLookUpTable )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For applicationtheme 
--

CREATE TABLE IF NOT EXISTS `applicationtheme_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idApplicationTheme`  int(10)  NULL DEFAULT NULL
 ,`applicationTheme`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For applicationtheme 
--


CREATE TRIGGER TrAI_applicationtheme_FER AFTER INSERT ON applicationtheme FOR EACH ROW
BEGIN
  INSERT INTO applicationtheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idApplicationTheme
  , NEW.applicationTheme
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_applicationtheme_FER AFTER UPDATE ON applicationtheme FOR EACH ROW
BEGIN
  INSERT INTO applicationtheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idApplicationTheme
  , NEW.applicationTheme
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_applicationtheme_FER AFTER DELETE ON applicationtheme FOR EACH ROW
BEGIN
  INSERT INTO applicationtheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idApplicationTheme
  , OLD.applicationTheme
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For applicationtype 
--

CREATE TABLE IF NOT EXISTS `applicationtype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeApplicationType`  varchar(10)  NULL DEFAULT NULL
 ,`applicationType`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For applicationtype 
--


CREATE TRIGGER TrAI_applicationtype_FER AFTER INSERT ON applicationtype FOR EACH ROW
BEGIN
  INSERT INTO applicationtype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeApplicationType
  , applicationType )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeApplicationType
  , NEW.applicationType );
END;
$$


CREATE TRIGGER TrAU_applicationtype_FER AFTER UPDATE ON applicationtype FOR EACH ROW
BEGIN
  INSERT INTO applicationtype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeApplicationType
  , applicationType )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeApplicationType
  , NEW.applicationType );
END;
$$


CREATE TRIGGER TrAD_applicationtype_FER AFTER DELETE ON applicationtype FOR EACH ROW
BEGIN
  INSERT INTO applicationtype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeApplicationType
  , applicationType )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeApplicationType
  , OLD.applicationType );
END;
$$


--
-- Audit Table For application 
--

CREATE TABLE IF NOT EXISTS `application_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For application 
--


CREATE TRIGGER TrAI_application_FER AFTER INSERT ON application FOR EACH ROW
BEGIN
  INSERT INTO application_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_application_FER AFTER UPDATE ON application FOR EACH ROW
BEGIN
  INSERT INTO application_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_application_FER AFTER DELETE ON application FOR EACH ROW
BEGIN
  INSERT INTO application_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For appuser 
--

CREATE TABLE IF NOT EXISTS `appuser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For appuser 
--


CREATE TRIGGER TrAI_appuser_FER AFTER INSERT ON appuser FOR EACH ROW
BEGIN
  INSERT INTO appuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_appuser_FER AFTER UPDATE ON appuser FOR EACH ROW
BEGIN
  INSERT INTO appuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_appuser_FER AFTER DELETE ON appuser FOR EACH ROW
BEGIN
  INSERT INTO appuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For arraycoordinate 
--

CREATE TABLE IF NOT EXISTS `arraycoordinate_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`x`  int(10)  NULL DEFAULT NULL
 ,`y`  int(10)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For arraycoordinate 
--


CREATE TRIGGER TrAI_arraycoordinate_FER AFTER INSERT ON arraycoordinate FOR EACH ROW
BEGIN
  INSERT INTO arraycoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idArrayCoordinate
  , NEW.name
  , NEW.x
  , NEW.y
  , NEW.idSlideDesign );
END;
$$


CREATE TRIGGER TrAU_arraycoordinate_FER AFTER UPDATE ON arraycoordinate FOR EACH ROW
BEGIN
  INSERT INTO arraycoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idArrayCoordinate
  , NEW.name
  , NEW.x
  , NEW.y
  , NEW.idSlideDesign );
END;
$$


CREATE TRIGGER TrAD_arraycoordinate_FER AFTER DELETE ON arraycoordinate FOR EACH ROW
BEGIN
  INSERT INTO arraycoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idArrayCoordinate
  , name
  , x
  , y
  , idSlideDesign )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idArrayCoordinate
  , OLD.name
  , OLD.x
  , OLD.y
  , OLD.idSlideDesign );
END;
$$


--
-- Audit Table For arraydesign 
--

CREATE TABLE IF NOT EXISTS `arraydesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idArrayDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`accessionNumberUArrayExpress`  varchar(100)  NULL DEFAULT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For arraydesign 
--


CREATE TRIGGER TrAI_arraydesign_FER AFTER INSERT ON arraydesign FOR EACH ROW
BEGIN
  INSERT INTO arraydesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idArrayDesign
  , NEW.name
  , NEW.accessionNumberUArrayExpress
  , NEW.idArrayCoordinate );
END;
$$


CREATE TRIGGER TrAU_arraydesign_FER AFTER UPDATE ON arraydesign FOR EACH ROW
BEGIN
  INSERT INTO arraydesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idArrayDesign
  , NEW.name
  , NEW.accessionNumberUArrayExpress
  , NEW.idArrayCoordinate );
END;
$$


CREATE TRIGGER TrAD_arraydesign_FER AFTER DELETE ON arraydesign FOR EACH ROW
BEGIN
  INSERT INTO arraydesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idArrayDesign
  , OLD.name
  , OLD.accessionNumberUArrayExpress
  , OLD.idArrayCoordinate );
END;
$$


--
-- Audit Table For assay 
--

CREATE TABLE IF NOT EXISTS `assay_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAssay`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For assay 
--


CREATE TRIGGER TrAI_assay_FER AFTER INSERT ON assay FOR EACH ROW
BEGIN
  INSERT INTO assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idAssay
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_assay_FER AFTER UPDATE ON assay FOR EACH ROW
BEGIN
  INSERT INTO assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idAssay
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_assay_FER AFTER DELETE ON assay FOR EACH ROW
BEGIN
  INSERT INTO assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idAssay
  , OLD.name
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For billingaccountuser 
--

CREATE TABLE IF NOT EXISTS `billingaccountuser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingaccountuser 
--


CREATE TRIGGER TrAI_billingaccountuser_FER AFTER INSERT ON billingaccountuser FOR EACH ROW
BEGIN
  INSERT INTO billingaccountuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingAccount
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingAccount
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_billingaccountuser_FER AFTER UPDATE ON billingaccountuser FOR EACH ROW
BEGIN
  INSERT INTO billingaccountuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingAccount
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingAccount
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_billingaccountuser_FER AFTER DELETE ON billingaccountuser FOR EACH ROW
BEGIN
  INSERT INTO billingaccountuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingAccount
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idBillingAccount
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For billingaccount 
--

CREATE TABLE IF NOT EXISTS `billingaccount_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingaccount 
--


CREATE TRIGGER TrAI_billingaccount_FER AFTER INSERT ON billingaccount FOR EACH ROW
BEGIN
  INSERT INTO billingaccount_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_billingaccount_FER AFTER UPDATE ON billingaccount FOR EACH ROW
BEGIN
  INSERT INTO billingaccount_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_billingaccount_FER AFTER DELETE ON billingaccount FOR EACH ROW
BEGIN
  INSERT INTO billingaccount_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For billingchargekind 
--

CREATE TABLE IF NOT EXISTS `billingchargekind_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`billingChargeKind`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingchargekind 
--


CREATE TRIGGER TrAI_billingchargekind_FER AFTER INSERT ON billingchargekind FOR EACH ROW
BEGIN
  INSERT INTO billingchargekind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeBillingChargeKind
  , NEW.billingChargeKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_billingchargekind_FER AFTER UPDATE ON billingchargekind FOR EACH ROW
BEGIN
  INSERT INTO billingchargekind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeBillingChargeKind
  , NEW.billingChargeKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_billingchargekind_FER AFTER DELETE ON billingchargekind FOR EACH ROW
BEGIN
  INSERT INTO billingchargekind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeBillingChargeKind
  , OLD.billingChargeKind
  , OLD.isActive );
END;
$$


--
-- Audit Table For billingitem 
--

CREATE TABLE IF NOT EXISTS `billingitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
 ,`idProductLineItem`  int(10)  NULL DEFAULT NULL
 ,`tag`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingitem 
--


CREATE TRIGGER TrAI_billingitem_FER AFTER INSERT ON billingitem FOR EACH ROW
BEGIN
  INSERT INTO billingitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , idProductLineItem
  , tag )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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
  , NEW.idProductLineItem
  , NEW.tag );
END;
$$


CREATE TRIGGER TrAU_billingitem_FER AFTER UPDATE ON billingitem FOR EACH ROW
BEGIN
  INSERT INTO billingitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , idProductLineItem
  , tag )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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
  , NEW.idProductLineItem
  , NEW.tag );
END;
$$


CREATE TRIGGER TrAD_billingitem_FER AFTER DELETE ON billingitem FOR EACH ROW
BEGIN
  INSERT INTO billingitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , idProductLineItem
  , tag )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
  , OLD.idProductLineItem
  , OLD.tag );
END;
$$


--
-- Audit Table For billingperiod 
--

CREATE TABLE IF NOT EXISTS `billingperiod_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`billingPeriod`  varchar(50)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`endDate`  datetime  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingperiod 
--


CREATE TRIGGER TrAI_billingperiod_FER AFTER INSERT ON billingperiod FOR EACH ROW
BEGIN
  INSERT INTO billingperiod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingPeriod
  , NEW.billingPeriod
  , NEW.startDate
  , NEW.endDate
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_billingperiod_FER AFTER UPDATE ON billingperiod FOR EACH ROW
BEGIN
  INSERT INTO billingperiod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingPeriod
  , NEW.billingPeriod
  , NEW.startDate
  , NEW.endDate
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_billingperiod_FER AFTER DELETE ON billingperiod FOR EACH ROW
BEGIN
  INSERT INTO billingperiod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idBillingPeriod
  , OLD.billingPeriod
  , OLD.startDate
  , OLD.endDate
  , OLD.isActive );
END;
$$


--
-- Audit Table For billingslideproductclass 
--

CREATE TABLE IF NOT EXISTS `billingslideproductclass_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingSlideProductClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideProductClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingslideproductclass 
--


CREATE TRIGGER TrAI_billingslideproductclass_FER AFTER INSERT ON billingslideproductclass FOR EACH ROW
BEGIN
  INSERT INTO billingslideproductclass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingSlideProductClass
  , NEW.billingSlideProductClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_billingslideproductclass_FER AFTER UPDATE ON billingslideproductclass FOR EACH ROW
BEGIN
  INSERT INTO billingslideproductclass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingSlideProductClass
  , NEW.billingSlideProductClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_billingslideproductclass_FER AFTER DELETE ON billingslideproductclass FOR EACH ROW
BEGIN
  INSERT INTO billingslideproductclass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idBillingSlideProductClass
  , OLD.billingSlideProductClass
  , OLD.isActive );
END;
$$


--
-- Audit Table For billingslideserviceclass 
--

CREATE TABLE IF NOT EXISTS `billingslideserviceclass_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideServiceClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingslideserviceclass 
--


CREATE TRIGGER TrAI_billingslideserviceclass_FER AFTER INSERT ON billingslideserviceclass FOR EACH ROW
BEGIN
  INSERT INTO billingslideserviceclass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingSlideServiceClass
  , NEW.billingSlideServiceClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_billingslideserviceclass_FER AFTER UPDATE ON billingslideserviceclass FOR EACH ROW
BEGIN
  INSERT INTO billingslideserviceclass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idBillingSlideServiceClass
  , NEW.billingSlideServiceClass
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_billingslideserviceclass_FER AFTER DELETE ON billingslideserviceclass FOR EACH ROW
BEGIN
  INSERT INTO billingslideserviceclass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idBillingSlideServiceClass
  , OLD.billingSlideServiceClass
  , OLD.isActive );
END;
$$


--
-- Audit Table For billingstatus 
--

CREATE TABLE IF NOT EXISTS `billingstatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBillingStatus`  varchar(10)  NULL DEFAULT NULL
 ,`billingStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For billingstatus 
--


CREATE TRIGGER TrAI_billingstatus_FER AFTER INSERT ON billingstatus FOR EACH ROW
BEGIN
  INSERT INTO billingstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeBillingStatus
  , NEW.billingStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_billingstatus_FER AFTER UPDATE ON billingstatus FOR EACH ROW
BEGIN
  INSERT INTO billingstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeBillingStatus
  , NEW.billingStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_billingstatus_FER AFTER DELETE ON billingstatus FOR EACH ROW
BEGIN
  INSERT INTO billingstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeBillingStatus
  , OLD.billingStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For bioanalyzerchiptype 
--

CREATE TABLE IF NOT EXISTS `bioanalyzerchiptype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For bioanalyzerchiptype 
--


CREATE TRIGGER TrAI_bioanalyzerchiptype_FER AFTER INSERT ON bioanalyzerchiptype FOR EACH ROW
BEGIN
  INSERT INTO bioanalyzerchiptype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeBioanalyzerChipType
  , NEW.bioanalyzerChipType
  , NEW.concentrationRange
  , NEW.maxSampleBufferStrength
  , NEW.costPerSample
  , NEW.sampleWellsPerChip
  , NEW.isActive
  , NEW.codeConcentrationUnit
  , NEW.codeApplication
  , NEW.protocolDescription );
END;
$$


CREATE TRIGGER TrAU_bioanalyzerchiptype_FER AFTER UPDATE ON bioanalyzerchiptype FOR EACH ROW
BEGIN
  INSERT INTO bioanalyzerchiptype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeBioanalyzerChipType
  , NEW.bioanalyzerChipType
  , NEW.concentrationRange
  , NEW.maxSampleBufferStrength
  , NEW.costPerSample
  , NEW.sampleWellsPerChip
  , NEW.isActive
  , NEW.codeConcentrationUnit
  , NEW.codeApplication
  , NEW.protocolDescription );
END;
$$


CREATE TRIGGER TrAD_bioanalyzerchiptype_FER AFTER DELETE ON bioanalyzerchiptype FOR EACH ROW
BEGIN
  INSERT INTO bioanalyzerchiptype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBioanalyzerChipType
  , bioanalyzerChipType
  , concentrationRange
  , maxSampleBufferStrength
  , costPerSample
  , sampleWellsPerChip
  , isActive
  , codeConcentrationUnit
  , codeApplication
  , protocolDescription )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeBioanalyzerChipType
  , OLD.bioanalyzerChipType
  , OLD.concentrationRange
  , OLD.maxSampleBufferStrength
  , OLD.costPerSample
  , OLD.sampleWellsPerChip
  , OLD.isActive
  , OLD.codeConcentrationUnit
  , OLD.codeApplication
  , OLD.protocolDescription );
END;
$$


--
-- Audit Table For chromatogram 
--

CREATE TABLE IF NOT EXISTS `chromatogram_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idChromatogram`  int(10)  NULL DEFAULT NULL
 ,`idPlateWell`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`displayName`  varchar(200)  NULL DEFAULT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For chromatogram 
--


CREATE TRIGGER TrAI_chromatogram_FER AFTER INSERT ON chromatogram FOR EACH ROW
BEGIN
  INSERT INTO chromatogram_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idChromatogram
  , idPlateWell
  , idRequest
  , displayName
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idChromatogram
  , NEW.idPlateWell
  , NEW.idRequest
  , NEW.displayName
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


CREATE TRIGGER TrAU_chromatogram_FER AFTER UPDATE ON chromatogram FOR EACH ROW
BEGIN
  INSERT INTO chromatogram_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idChromatogram
  , idPlateWell
  , idRequest
  , displayName
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idChromatogram
  , NEW.idPlateWell
  , NEW.idRequest
  , NEW.displayName
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


CREATE TRIGGER TrAD_chromatogram_FER AFTER DELETE ON chromatogram FOR EACH ROW
BEGIN
  INSERT INTO chromatogram_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idChromatogram
  , idPlateWell
  , idRequest
  , displayName
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idChromatogram
  , OLD.idPlateWell
  , OLD.idRequest
  , OLD.displayName
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
-- Audit Table For concentrationunit 
--

CREATE TABLE IF NOT EXISTS `concentrationunit_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`concentrationUnit`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For concentrationunit 
--


CREATE TRIGGER TrAI_concentrationunit_FER AFTER INSERT ON concentrationunit FOR EACH ROW
BEGIN
  INSERT INTO concentrationunit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeConcentrationUnit
  , NEW.concentrationUnit
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_concentrationunit_FER AFTER UPDATE ON concentrationunit FOR EACH ROW
BEGIN
  INSERT INTO concentrationunit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeConcentrationUnit
  , NEW.concentrationUnit
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_concentrationunit_FER AFTER DELETE ON concentrationunit FOR EACH ROW
BEGIN
  INSERT INTO concentrationunit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeConcentrationUnit
  , concentrationUnit
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeConcentrationUnit
  , OLD.concentrationUnit
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive );
END;
$$


--
-- Audit Table For contextsensitivehelp 
--

CREATE TABLE IF NOT EXISTS `contextsensitivehelp_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idContextSensitiveHelp`  int(10)  NULL DEFAULT NULL
 ,`context1`  varchar(100)  NULL DEFAULT NULL
 ,`context2`  varchar(100)  NULL DEFAULT NULL
 ,`context3`  varchar(100)  NULL DEFAULT NULL
 ,`helpText`  varchar(10000)  NULL DEFAULT NULL
 ,`toolTipText`  varchar(10000)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For contextsensitivehelp 
--


CREATE TRIGGER TrAI_contextsensitivehelp_FER AFTER INSERT ON contextsensitivehelp FOR EACH ROW
BEGIN
  INSERT INTO contextsensitivehelp_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idContextSensitiveHelp
  , NEW.context1
  , NEW.context2
  , NEW.context3
  , NEW.helpText
  , NEW.toolTipText );
END;
$$


CREATE TRIGGER TrAU_contextsensitivehelp_FER AFTER UPDATE ON contextsensitivehelp FOR EACH ROW
BEGIN
  INSERT INTO contextsensitivehelp_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idContextSensitiveHelp
  , NEW.context1
  , NEW.context2
  , NEW.context3
  , NEW.helpText
  , NEW.toolTipText );
END;
$$


CREATE TRIGGER TrAD_contextsensitivehelp_FER AFTER DELETE ON contextsensitivehelp FOR EACH ROW
BEGIN
  INSERT INTO contextsensitivehelp_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idContextSensitiveHelp
  , context1
  , context2
  , context3
  , helpText
  , toolTipText )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idContextSensitiveHelp
  , OLD.context1
  , OLD.context2
  , OLD.context3
  , OLD.helpText
  , OLD.toolTipText );
END;
$$


--
-- Audit Table For corefacilitylab 
--

CREATE TABLE IF NOT EXISTS `corefacilitylab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For corefacilitylab 
--


CREATE TRIGGER TrAI_corefacilitylab_FER AFTER INSERT ON corefacilitylab FOR EACH ROW
BEGIN
  INSERT INTO corefacilitylab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idLab )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idCoreFacility
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAU_corefacilitylab_FER AFTER UPDATE ON corefacilitylab FOR EACH ROW
BEGIN
  INSERT INTO corefacilitylab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idLab )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idCoreFacility
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAD_corefacilitylab_FER AFTER DELETE ON corefacilitylab FOR EACH ROW
BEGIN
  INSERT INTO corefacilitylab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idLab )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idCoreFacility
  , OLD.idLab );
END;
$$


--
-- Audit Table For corefacilitymanager 
--

CREATE TABLE IF NOT EXISTS `corefacilitymanager_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For corefacilitymanager 
--


CREATE TRIGGER TrAI_corefacilitymanager_FER AFTER INSERT ON corefacilitymanager FOR EACH ROW
BEGIN
  INSERT INTO corefacilitymanager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_corefacilitymanager_FER AFTER UPDATE ON corefacilitymanager FOR EACH ROW
BEGIN
  INSERT INTO corefacilitymanager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_corefacilitymanager_FER AFTER DELETE ON corefacilitymanager FOR EACH ROW
BEGIN
  INSERT INTO corefacilitymanager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idCoreFacility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For corefacilitysubmitter 
--

CREATE TABLE IF NOT EXISTS `corefacilitysubmitter_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For corefacilitysubmitter 
--


CREATE TRIGGER TrAI_corefacilitysubmitter_FER AFTER INSERT ON corefacilitysubmitter FOR EACH ROW
BEGIN
  INSERT INTO corefacilitysubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_corefacilitysubmitter_FER AFTER UPDATE ON corefacilitysubmitter FOR EACH ROW
BEGIN
  INSERT INTO corefacilitysubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idCoreFacility
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_corefacilitysubmitter_FER AFTER DELETE ON corefacilitysubmitter FOR EACH ROW
BEGIN
  INSERT INTO corefacilitysubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idCoreFacility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For corefacility 
--

CREATE TABLE IF NOT EXISTS `corefacility_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For corefacility 
--


CREATE TRIGGER TrAI_corefacility_FER AFTER INSERT ON corefacility FOR EACH ROW
BEGIN
  INSERT INTO corefacility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_corefacility_FER AFTER UPDATE ON corefacility FOR EACH ROW
BEGIN
  INSERT INTO corefacility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_corefacility_FER AFTER DELETE ON corefacility FOR EACH ROW
BEGIN
  INSERT INTO corefacility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For creditcardcompany 
--

CREATE TABLE IF NOT EXISTS `creditcardcompany_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCreditCardCompany`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  varchar(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For creditcardcompany 
--


CREATE TRIGGER TrAI_creditcardcompany_FER AFTER INSERT ON creditcardcompany FOR EACH ROW
BEGIN
  INSERT INTO creditcardcompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idCreditCardCompany
  , NEW.name
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_creditcardcompany_FER AFTER UPDATE ON creditcardcompany FOR EACH ROW
BEGIN
  INSERT INTO creditcardcompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idCreditCardCompany
  , NEW.name
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_creditcardcompany_FER AFTER DELETE ON creditcardcompany FOR EACH ROW
BEGIN
  INSERT INTO creditcardcompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idCreditCardCompany
  , OLD.name
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For datatrackcollaborator 
--

CREATE TABLE IF NOT EXISTS `datatrackcollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For datatrackcollaborator 
--


CREATE TRIGGER TrAI_datatrackcollaborator_FER AFTER INSERT ON datatrackcollaborator FOR EACH ROW
BEGIN
  INSERT INTO datatrackcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idDataTrack
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_datatrackcollaborator_FER AFTER UPDATE ON datatrackcollaborator FOR EACH ROW
BEGIN
  INSERT INTO datatrackcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idDataTrack
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_datatrackcollaborator_FER AFTER DELETE ON datatrackcollaborator FOR EACH ROW
BEGIN
  INSERT INTO datatrackcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idDataTrack
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For datatrackfile 
--

CREATE TABLE IF NOT EXISTS `datatrackfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrackFile`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For datatrackfile 
--


CREATE TRIGGER TrAI_datatrackfile_FER AFTER INSERT ON datatrackfile FOR EACH ROW
BEGIN
  INSERT INTO datatrackfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idDataTrackFile
  , NEW.idAnalysisFile
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAU_datatrackfile_FER AFTER UPDATE ON datatrackfile FOR EACH ROW
BEGIN
  INSERT INTO datatrackfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idDataTrackFile
  , NEW.idAnalysisFile
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAD_datatrackfile_FER AFTER DELETE ON datatrackfile FOR EACH ROW
BEGIN
  INSERT INTO datatrackfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idDataTrackFile
  , OLD.idAnalysisFile
  , OLD.idDataTrack );
END;
$$


--
-- Audit Table For datatrackfolder 
--

CREATE TABLE IF NOT EXISTS `datatrackfolder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`idParentDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For datatrackfolder 
--


CREATE TRIGGER TrAI_datatrackfolder_FER AFTER INSERT ON datatrackfolder FOR EACH ROW
BEGIN
  INSERT INTO datatrackfolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_datatrackfolder_FER AFTER UPDATE ON datatrackfolder FOR EACH ROW
BEGIN
  INSERT INTO datatrackfolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_datatrackfolder_FER AFTER DELETE ON datatrackfolder FOR EACH ROW
BEGIN
  INSERT INTO datatrackfolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrackFolder
  , name
  , description
  , idParentDataTrackFolder
  , idGenomeBuild
  , idLab
  , createdBy
  , createDate )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For datatracktofolder 
--

CREATE TABLE IF NOT EXISTS `datatracktofolder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For datatracktofolder 
--


CREATE TRIGGER TrAI_datatracktofolder_FER AFTER INSERT ON datatracktofolder FOR EACH ROW
BEGIN
  INSERT INTO datatracktofolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idDataTrack
  , NEW.idDataTrackFolder );
END;
$$


CREATE TRIGGER TrAU_datatracktofolder_FER AFTER UPDATE ON datatracktofolder FOR EACH ROW
BEGIN
  INSERT INTO datatracktofolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idDataTrack
  , NEW.idDataTrackFolder );
END;
$$


CREATE TRIGGER TrAD_datatracktofolder_FER AFTER DELETE ON datatracktofolder FOR EACH ROW
BEGIN
  INSERT INTO datatracktofolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idDataTrack
  , OLD.idDataTrackFolder );
END;
$$


--
-- Audit Table For datatracktotopic 
--

CREATE TABLE IF NOT EXISTS `datatracktotopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For datatracktotopic 
--


CREATE TRIGGER TrAI_datatracktotopic_FER AFTER INSERT ON datatracktotopic FOR EACH ROW
BEGIN
  INSERT INTO datatracktotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idDataTrack )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idTopic
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAU_datatracktotopic_FER AFTER UPDATE ON datatracktotopic FOR EACH ROW
BEGIN
  INSERT INTO datatracktotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idDataTrack )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idTopic
  , NEW.idDataTrack );
END;
$$


CREATE TRIGGER TrAD_datatracktotopic_FER AFTER DELETE ON datatracktotopic FOR EACH ROW
BEGIN
  INSERT INTO datatracktotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idDataTrack )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idTopic
  , OLD.idDataTrack );
END;
$$


--
-- Audit Table For datatrack 
--

CREATE TABLE IF NOT EXISTS `datatrack_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For datatrack 
--


CREATE TRIGGER TrAI_datatrack_FER AFTER INSERT ON datatrack FOR EACH ROW
BEGIN
  INSERT INTO datatrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_datatrack_FER AFTER UPDATE ON datatrack FOR EACH ROW
BEGIN
  INSERT INTO datatrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_datatrack_FER AFTER DELETE ON datatrack FOR EACH ROW
BEGIN
  INSERT INTO datatrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For diskusagebymonth 
--

CREATE TABLE IF NOT EXISTS `diskusagebymonth_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For diskusagebymonth 
--


CREATE TRIGGER TrAI_diskusagebymonth_FER AFTER INSERT ON diskusagebymonth FOR EACH ROW
BEGIN
  INSERT INTO diskusagebymonth_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_diskusagebymonth_FER AFTER UPDATE ON diskusagebymonth FOR EACH ROW
BEGIN
  INSERT INTO diskusagebymonth_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_diskusagebymonth_FER AFTER DELETE ON diskusagebymonth FOR EACH ROW
BEGIN
  INSERT INTO diskusagebymonth_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For experimentdesignentry 
--

CREATE TABLE IF NOT EXISTS `experimentdesignentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentDesignEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For experimentdesignentry 
--


CREATE TRIGGER TrAI_experimentdesignentry_FER AFTER INSERT ON experimentdesignentry FOR EACH ROW
BEGIN
  INSERT INTO experimentdesignentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idExperimentDesignEntry
  , NEW.codeExperimentDesign
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_experimentdesignentry_FER AFTER UPDATE ON experimentdesignentry FOR EACH ROW
BEGIN
  INSERT INTO experimentdesignentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idExperimentDesignEntry
  , NEW.codeExperimentDesign
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_experimentdesignentry_FER AFTER DELETE ON experimentdesignentry FOR EACH ROW
BEGIN
  INSERT INTO experimentdesignentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentDesignEntry
  , codeExperimentDesign
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idExperimentDesignEntry
  , OLD.codeExperimentDesign
  , OLD.idProject
  , OLD.valueString
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For experimentdesign 
--

CREATE TABLE IF NOT EXISTS `experimentdesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`experimentDesign`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For experimentdesign 
--


CREATE TRIGGER TrAI_experimentdesign_FER AFTER INSERT ON experimentdesign FOR EACH ROW
BEGIN
  INSERT INTO experimentdesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeExperimentDesign
  , NEW.experimentDesign
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_experimentdesign_FER AFTER UPDATE ON experimentdesign FOR EACH ROW
BEGIN
  INSERT INTO experimentdesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeExperimentDesign
  , NEW.experimentDesign
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_experimentdesign_FER AFTER DELETE ON experimentdesign FOR EACH ROW
BEGIN
  INSERT INTO experimentdesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeExperimentDesign
  , experimentDesign
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeExperimentDesign
  , OLD.experimentDesign
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For experimentfactorentry 
--

CREATE TABLE IF NOT EXISTS `experimentfactorentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentFactorEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For experimentfactorentry 
--


CREATE TRIGGER TrAI_experimentfactorentry_FER AFTER INSERT ON experimentfactorentry FOR EACH ROW
BEGIN
  INSERT INTO experimentfactorentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idExperimentFactorEntry
  , NEW.codeExperimentFactor
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_experimentfactorentry_FER AFTER UPDATE ON experimentfactorentry FOR EACH ROW
BEGIN
  INSERT INTO experimentfactorentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idExperimentFactorEntry
  , NEW.codeExperimentFactor
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_experimentfactorentry_FER AFTER DELETE ON experimentfactorentry FOR EACH ROW
BEGIN
  INSERT INTO experimentfactorentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentFactorEntry
  , codeExperimentFactor
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idExperimentFactorEntry
  , OLD.codeExperimentFactor
  , OLD.idProject
  , OLD.valueString
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For experimentfactor 
--

CREATE TABLE IF NOT EXISTS `experimentfactor_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`experimentFactor`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For experimentfactor 
--


CREATE TRIGGER TrAI_experimentfactor_FER AFTER INSERT ON experimentfactor FOR EACH ROW
BEGIN
  INSERT INTO experimentfactor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeExperimentFactor
  , NEW.experimentFactor
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_experimentfactor_FER AFTER UPDATE ON experimentfactor FOR EACH ROW
BEGIN
  INSERT INTO experimentfactor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeExperimentFactor
  , NEW.experimentFactor
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_experimentfactor_FER AFTER DELETE ON experimentfactor FOR EACH ROW
BEGIN
  INSERT INTO experimentfactor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeExperimentFactor
  , experimentFactor
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeExperimentFactor
  , OLD.experimentFactor
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For experimentfile 
--

CREATE TABLE IF NOT EXISTS `experimentfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For experimentfile 
--


CREATE TRIGGER TrAI_experimentfile_FER AFTER INSERT ON experimentfile FOR EACH ROW
BEGIN
  INSERT INTO experimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idExperimentFile
  , NEW.fileName
  , NEW.fileSize
  , NEW.idRequest
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAU_experimentfile_FER AFTER UPDATE ON experimentfile FOR EACH ROW
BEGIN
  INSERT INTO experimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idExperimentFile
  , NEW.fileName
  , NEW.fileSize
  , NEW.idRequest
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAD_experimentfile_FER AFTER DELETE ON experimentfile FOR EACH ROW
BEGIN
  INSERT INTO experimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idExperimentFile
  , fileName
  , fileSize
  , idRequest
  , createDate )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idExperimentFile
  , OLD.fileName
  , OLD.fileSize
  , OLD.idRequest
  , OLD.createDate );
END;
$$


--
-- Audit Table For faq 
--

CREATE TABLE IF NOT EXISTS `faq_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFAQ`  int(10)  NULL DEFAULT NULL
 ,`title`  varchar(300)  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For faq 
--


CREATE TRIGGER TrAI_faq_FER AFTER INSERT ON faq FOR EACH ROW
BEGIN
  INSERT INTO faq_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idFAQ
  , NEW.title
  , NEW.url
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_faq_FER AFTER UPDATE ON faq FOR EACH ROW
BEGIN
  INSERT INTO faq_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idFAQ
  , NEW.title
  , NEW.url
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_faq_FER AFTER DELETE ON faq FOR EACH ROW
BEGIN
  INSERT INTO faq_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idFAQ
  , OLD.title
  , OLD.url
  , OLD.idCoreFacility );
END;
$$


--
-- Audit Table For featureextractionprotocol 
--

CREATE TABLE IF NOT EXISTS `featureextractionprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFeatureExtractionProtocol`  int(10)  NULL DEFAULT NULL
 ,`featureExtractionProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For featureextractionprotocol 
--


CREATE TRIGGER TrAI_featureextractionprotocol_FER AFTER INSERT ON featureextractionprotocol FOR EACH ROW
BEGIN
  INSERT INTO featureextractionprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idFeatureExtractionProtocol
  , NEW.featureExtractionProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_featureextractionprotocol_FER AFTER UPDATE ON featureextractionprotocol FOR EACH ROW
BEGIN
  INSERT INTO featureextractionprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idFeatureExtractionProtocol
  , NEW.featureExtractionProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_featureextractionprotocol_FER AFTER DELETE ON featureextractionprotocol FOR EACH ROW
BEGIN
  INSERT INTO featureextractionprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFeatureExtractionProtocol
  , featureExtractionProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idFeatureExtractionProtocol
  , OLD.featureExtractionProtocol
  , OLD.codeRequestCategory
  , OLD.description
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For flowcellchannel 
--

CREATE TABLE IF NOT EXISTS `flowcellchannel_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For flowcellchannel 
--


CREATE TRIGGER TrAI_flowcellchannel_FER AFTER INSERT ON flowcellchannel FOR EACH ROW
BEGIN
  INSERT INTO flowcellchannel_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , q30Percent )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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
  , NEW.q30Percent );
END;
$$


CREATE TRIGGER TrAU_flowcellchannel_FER AFTER UPDATE ON flowcellchannel FOR EACH ROW
BEGIN
  INSERT INTO flowcellchannel_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , q30Percent )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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
  , NEW.q30Percent );
END;
$$


CREATE TRIGGER TrAD_flowcellchannel_FER AFTER DELETE ON flowcellchannel FOR EACH ROW
BEGIN
  INSERT INTO flowcellchannel_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , q30Percent )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
  , OLD.q30Percent );
END;
$$


--
-- Audit Table For flowcell 
--

CREATE TABLE IF NOT EXISTS `flowcell_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For flowcell 
--


CREATE TRIGGER TrAI_flowcell_FER AFTER INSERT ON flowcell FOR EACH ROW
BEGIN
  INSERT INTO flowcell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_flowcell_FER AFTER UPDATE ON flowcell FOR EACH ROW
BEGIN
  INSERT INTO flowcell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_flowcell_FER AFTER DELETE ON flowcell FOR EACH ROW
BEGIN
  INSERT INTO flowcell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For fundingagency 
--

CREATE TABLE IF NOT EXISTS `fundingagency_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFundingAgency`  int(10)  NULL DEFAULT NULL
 ,`fundingAgency`  varchar(200)  NULL DEFAULT NULL
 ,`isPeerReviewedFunding`  char(1)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For fundingagency 
--


CREATE TRIGGER TrAI_fundingagency_FER AFTER INSERT ON fundingagency FOR EACH ROW
BEGIN
  INSERT INTO fundingagency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idFundingAgency
  , NEW.fundingAgency
  , NEW.isPeerReviewedFunding
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_fundingagency_FER AFTER UPDATE ON fundingagency FOR EACH ROW
BEGIN
  INSERT INTO fundingagency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idFundingAgency
  , NEW.fundingAgency
  , NEW.isPeerReviewedFunding
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_fundingagency_FER AFTER DELETE ON fundingagency FOR EACH ROW
BEGIN
  INSERT INTO fundingagency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idFundingAgency
  , OLD.fundingAgency
  , OLD.isPeerReviewedFunding
  , OLD.isActive );
END;
$$


--
-- Audit Table For genomebuildalias 
--

CREATE TABLE IF NOT EXISTS `genomebuildalias_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeBuildAlias`  int(10)  NULL DEFAULT NULL
 ,`alias`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For genomebuildalias 
--


CREATE TRIGGER TrAI_genomebuildalias_FER AFTER INSERT ON genomebuildalias FOR EACH ROW
BEGIN
  INSERT INTO genomebuildalias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idGenomeBuildAlias
  , NEW.alias
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAU_genomebuildalias_FER AFTER UPDATE ON genomebuildalias FOR EACH ROW
BEGIN
  INSERT INTO genomebuildalias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idGenomeBuildAlias
  , NEW.alias
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAD_genomebuildalias_FER AFTER DELETE ON genomebuildalias FOR EACH ROW
BEGIN
  INSERT INTO genomebuildalias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idGenomeBuildAlias
  , OLD.alias
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For genomebuild 
--

CREATE TABLE IF NOT EXISTS `genomebuild_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For genomebuild 
--


CREATE TRIGGER TrAI_genomebuild_FER AFTER INSERT ON genomebuild FOR EACH ROW
BEGIN
  INSERT INTO genomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_genomebuild_FER AFTER UPDATE ON genomebuild FOR EACH ROW
BEGIN
  INSERT INTO genomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_genomebuild_FER AFTER DELETE ON genomebuild FOR EACH ROW
BEGIN
  INSERT INTO genomebuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For genomeindex 
--

CREATE TABLE IF NOT EXISTS `genomeindex_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
 ,`genomeIndexName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For genomeindex 
--


CREATE TRIGGER TrAI_genomeindex_FER AFTER INSERT ON genomeindex FOR EACH ROW
BEGIN
  INSERT INTO genomeindex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idGenomeIndex
  , NEW.genomeIndexName
  , NEW.webServiceName
  , NEW.isActive
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAU_genomeindex_FER AFTER UPDATE ON genomeindex FOR EACH ROW
BEGIN
  INSERT INTO genomeindex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idGenomeIndex
  , NEW.genomeIndexName
  , NEW.webServiceName
  , NEW.isActive
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAD_genomeindex_FER AFTER DELETE ON genomeindex FOR EACH ROW
BEGIN
  INSERT INTO genomeindex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idGenomeIndex
  , genomeIndexName
  , webServiceName
  , isActive
  , idOrganism )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idGenomeIndex
  , OLD.genomeIndexName
  , OLD.webServiceName
  , OLD.isActive
  , OLD.idOrganism );
END;
$$


--
-- Audit Table For hybprotocol 
--

CREATE TABLE IF NOT EXISTS `hybprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idHybProtocol`  int(10)  NULL DEFAULT NULL
 ,`hybProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For hybprotocol 
--


CREATE TRIGGER TrAI_hybprotocol_FER AFTER INSERT ON hybprotocol FOR EACH ROW
BEGIN
  INSERT INTO hybprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idHybProtocol
  , NEW.hybProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_hybprotocol_FER AFTER UPDATE ON hybprotocol FOR EACH ROW
BEGIN
  INSERT INTO hybprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idHybProtocol
  , NEW.hybProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_hybprotocol_FER AFTER DELETE ON hybprotocol FOR EACH ROW
BEGIN
  INSERT INTO hybprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idHybProtocol
  , hybProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idHybProtocol
  , OLD.hybProtocol
  , OLD.codeRequestCategory
  , OLD.description
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For hybridization 
--

CREATE TABLE IF NOT EXISTS `hybridization_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For hybridization 
--


CREATE TRIGGER TrAI_hybridization_FER AFTER INSERT ON hybridization FOR EACH ROW
BEGIN
  INSERT INTO hybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_hybridization_FER AFTER UPDATE ON hybridization FOR EACH ROW
BEGIN
  INSERT INTO hybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_hybridization_FER AFTER DELETE ON hybridization FOR EACH ROW
BEGIN
  INSERT INTO hybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For institutionlab 
--

CREATE TABLE IF NOT EXISTS `institutionlab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For institutionlab 
--


CREATE TRIGGER TrAI_institutionlab_FER AFTER INSERT ON institutionlab FOR EACH ROW
BEGIN
  INSERT INTO institutionlab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstitution
  , idLab )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idInstitution
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAU_institutionlab_FER AFTER UPDATE ON institutionlab FOR EACH ROW
BEGIN
  INSERT INTO institutionlab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstitution
  , idLab )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idInstitution
  , NEW.idLab );
END;
$$


CREATE TRIGGER TrAD_institutionlab_FER AFTER DELETE ON institutionlab FOR EACH ROW
BEGIN
  INSERT INTO institutionlab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstitution
  , idLab )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idInstitution
  , OLD.idLab );
END;
$$


--
-- Audit Table For institution 
--

CREATE TABLE IF NOT EXISTS `institution_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`institution`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`isDefault`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For institution 
--


CREATE TRIGGER TrAI_institution_FER AFTER INSERT ON institution FOR EACH ROW
BEGIN
  INSERT INTO institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idInstitution
  , NEW.institution
  , NEW.description
  , NEW.isActive
  , NEW.isDefault );
END;
$$


CREATE TRIGGER TrAU_institution_FER AFTER UPDATE ON institution FOR EACH ROW
BEGIN
  INSERT INTO institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idInstitution
  , NEW.institution
  , NEW.description
  , NEW.isActive
  , NEW.isDefault );
END;
$$


CREATE TRIGGER TrAD_institution_FER AFTER DELETE ON institution FOR EACH ROW
BEGIN
  INSERT INTO institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstitution
  , institution
  , description
  , isActive
  , isDefault )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idInstitution
  , OLD.institution
  , OLD.description
  , OLD.isActive
  , OLD.isDefault );
END;
$$


--
-- Audit Table For instrumentrunstatus 
--

CREATE TABLE IF NOT EXISTS `instrumentrunstatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`instrumentRunStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For instrumentrunstatus 
--


CREATE TRIGGER TrAI_instrumentrunstatus_FER AFTER INSERT ON instrumentrunstatus FOR EACH ROW
BEGIN
  INSERT INTO instrumentrunstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeInstrumentRunStatus
  , NEW.instrumentRunStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_instrumentrunstatus_FER AFTER UPDATE ON instrumentrunstatus FOR EACH ROW
BEGIN
  INSERT INTO instrumentrunstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeInstrumentRunStatus
  , NEW.instrumentRunStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_instrumentrunstatus_FER AFTER DELETE ON instrumentrunstatus FOR EACH ROW
BEGIN
  INSERT INTO instrumentrunstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeInstrumentRunStatus
  , OLD.instrumentRunStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For instrumentrun 
--

CREATE TABLE IF NOT EXISTS `instrumentrun_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstrumentRun`  int(10)  NULL DEFAULT NULL
 ,`runDate`  datetime  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`comments`  varchar(200)  NULL DEFAULT NULL
 ,`label`  varchar(100)  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`creator`  varchar(50)  NULL DEFAULT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For instrumentrun 
--


CREATE TRIGGER TrAI_instrumentrun_FER AFTER INSERT ON instrumentrun FOR EACH ROW
BEGIN
  INSERT INTO instrumentrun_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_instrumentrun_FER AFTER UPDATE ON instrumentrun FOR EACH ROW
BEGIN
  INSERT INTO instrumentrun_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_instrumentrun_FER AFTER DELETE ON instrumentrun FOR EACH ROW
BEGIN
  INSERT INTO instrumentrun_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For instrument 
--

CREATE TABLE IF NOT EXISTS `instrument_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstrument`  int(10)  NULL DEFAULT NULL
 ,`instrument`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For instrument 
--


CREATE TRIGGER TrAI_instrument_FER AFTER INSERT ON instrument FOR EACH ROW
BEGIN
  INSERT INTO instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idInstrument
  , NEW.instrument
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_instrument_FER AFTER UPDATE ON instrument FOR EACH ROW
BEGIN
  INSERT INTO instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idInstrument
  , NEW.instrument
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_instrument_FER AFTER DELETE ON instrument FOR EACH ROW
BEGIN
  INSERT INTO instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idInstrument
  , OLD.instrument
  , OLD.isActive );
END;
$$


--
-- Audit Table For internalaccountfieldsconfiguration 
--

CREATE TABLE IF NOT EXISTS `internalaccountfieldsconfiguration_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInternalAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`displayName`  varchar(50)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
 ,`isNumber`  char(1)  NULL DEFAULT NULL
 ,`minLength`  int(10)  NULL DEFAULT NULL
 ,`maxLength`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For internalaccountfieldsconfiguration 
--


CREATE TRIGGER TrAI_internalaccountfieldsconfiguration_FER AFTER INSERT ON internalaccountfieldsconfiguration FOR EACH ROW
BEGIN
  INSERT INTO internalaccountfieldsconfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_internalaccountfieldsconfiguration_FER AFTER UPDATE ON internalaccountfieldsconfiguration FOR EACH ROW
BEGIN
  INSERT INTO internalaccountfieldsconfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_internalaccountfieldsconfiguration_FER AFTER DELETE ON internalaccountfieldsconfiguration FOR EACH ROW
BEGIN
  INSERT INTO internalaccountfieldsconfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For invoice 
--

CREATE TABLE IF NOT EXISTS `invoice_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`invoiceNumber`  varchar(50)  NULL DEFAULT NULL
 ,`lastEmailDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For invoice 
--


CREATE TRIGGER TrAI_invoice_FER AFTER INSERT ON invoice FOR EACH ROW
BEGIN
  INSERT INTO invoice_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idInvoice
  , NEW.idCoreFacility
  , NEW.idBillingPeriod
  , NEW.idBillingAccount
  , NEW.invoiceNumber
  , NEW.lastEmailDate );
END;
$$


CREATE TRIGGER TrAU_invoice_FER AFTER UPDATE ON invoice FOR EACH ROW
BEGIN
  INSERT INTO invoice_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idInvoice
  , NEW.idCoreFacility
  , NEW.idBillingPeriod
  , NEW.idBillingAccount
  , NEW.invoiceNumber
  , NEW.lastEmailDate );
END;
$$


CREATE TRIGGER TrAD_invoice_FER AFTER DELETE ON invoice FOR EACH ROW
BEGIN
  INSERT INTO invoice_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInvoice
  , idCoreFacility
  , idBillingPeriod
  , idBillingAccount
  , invoiceNumber
  , lastEmailDate )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idInvoice
  , OLD.idCoreFacility
  , OLD.idBillingPeriod
  , OLD.idBillingAccount
  , OLD.invoiceNumber
  , OLD.lastEmailDate );
END;
$$


--
-- Audit Table For iscanchip 
--

CREATE TABLE IF NOT EXISTS `iscanchip_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idIScanChip`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`costPerSample`  decimal(5,2)  NULL DEFAULT NULL
 ,`samplesPerChip`  int(10)  NULL DEFAULT NULL
 ,`chipsPerKit`  int(10)  NULL DEFAULT NULL
 ,`markersPerSample`  varchar(100)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For iscanchip 
--


CREATE TRIGGER TrAI_iscanchip_FER AFTER INSERT ON iscanchip FOR EACH ROW
BEGIN
  INSERT INTO iscanchip_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idIScanChip
  , name
  , costPerSample
  , samplesPerChip
  , chipsPerKit
  , markersPerSample
  , catalogNumber
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idIScanChip
  , NEW.name
  , NEW.costPerSample
  , NEW.samplesPerChip
  , NEW.chipsPerKit
  , NEW.markersPerSample
  , NEW.catalogNumber
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_iscanchip_FER AFTER UPDATE ON iscanchip FOR EACH ROW
BEGIN
  INSERT INTO iscanchip_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idIScanChip
  , name
  , costPerSample
  , samplesPerChip
  , chipsPerKit
  , markersPerSample
  , catalogNumber
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idIScanChip
  , NEW.name
  , NEW.costPerSample
  , NEW.samplesPerChip
  , NEW.chipsPerKit
  , NEW.markersPerSample
  , NEW.catalogNumber
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_iscanchip_FER AFTER DELETE ON iscanchip FOR EACH ROW
BEGIN
  INSERT INTO iscanchip_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idIScanChip
  , name
  , costPerSample
  , samplesPerChip
  , chipsPerKit
  , markersPerSample
  , catalogNumber
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idIScanChip
  , OLD.name
  , OLD.costPerSample
  , OLD.samplesPerChip
  , OLD.chipsPerKit
  , OLD.markersPerSample
  , OLD.catalogNumber
  , OLD.isActive );
END;
$$


--
-- Audit Table For isolationpreptype 
--

CREATE TABLE IF NOT EXISTS `isolationpreptype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeIsolationPrepType`  varchar(15)  NULL DEFAULT NULL
 ,`isolationPrepType`  varchar(100)  NULL DEFAULT NULL
 ,`type`  varchar(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For isolationpreptype 
--


CREATE TRIGGER TrAI_isolationpreptype_FER AFTER INSERT ON isolationpreptype FOR EACH ROW
BEGIN
  INSERT INTO isolationpreptype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeIsolationPrepType
  , NEW.isolationPrepType
  , NEW.type
  , NEW.isActive
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_isolationpreptype_FER AFTER UPDATE ON isolationpreptype FOR EACH ROW
BEGIN
  INSERT INTO isolationpreptype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeIsolationPrepType
  , NEW.isolationPrepType
  , NEW.type
  , NEW.isActive
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_isolationpreptype_FER AFTER DELETE ON isolationpreptype FOR EACH ROW
BEGIN
  INSERT INTO isolationpreptype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeIsolationPrepType
  , isolationPrepType
  , type
  , isActive
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeIsolationPrepType
  , OLD.isolationPrepType
  , OLD.type
  , OLD.isActive
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For labcollaborator 
--

CREATE TABLE IF NOT EXISTS `labcollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For labcollaborator 
--


CREATE TRIGGER TrAI_labcollaborator_FER AFTER INSERT ON labcollaborator FOR EACH ROW
BEGIN
  INSERT INTO labcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAU_labcollaborator_FER AFTER UPDATE ON labcollaborator FOR EACH ROW
BEGIN
  INSERT INTO labcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAD_labcollaborator_FER AFTER DELETE ON labcollaborator FOR EACH ROW
BEGIN
  INSERT INTO labcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For labeledsample 
--

CREATE TABLE IF NOT EXISTS `labeledsample_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For labeledsample 
--


CREATE TRIGGER TrAI_labeledsample_FER AFTER INSERT ON labeledsample FOR EACH ROW
BEGIN
  INSERT INTO labeledsample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_labeledsample_FER AFTER UPDATE ON labeledsample FOR EACH ROW
BEGIN
  INSERT INTO labeledsample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_labeledsample_FER AFTER DELETE ON labeledsample FOR EACH ROW
BEGIN
  INSERT INTO labeledsample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For labelingprotocol 
--

CREATE TABLE IF NOT EXISTS `labelingprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabelingProtocol`  int(10)  NULL DEFAULT NULL
 ,`labelingProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For labelingprotocol 
--


CREATE TRIGGER TrAI_labelingprotocol_FER AFTER INSERT ON labelingprotocol FOR EACH ROW
BEGIN
  INSERT INTO labelingprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idLabelingProtocol
  , NEW.labelingProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_labelingprotocol_FER AFTER UPDATE ON labelingprotocol FOR EACH ROW
BEGIN
  INSERT INTO labelingprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idLabelingProtocol
  , NEW.labelingProtocol
  , NEW.codeRequestCategory
  , NEW.description
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_labelingprotocol_FER AFTER DELETE ON labelingprotocol FOR EACH ROW
BEGIN
  INSERT INTO labelingprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLabelingProtocol
  , labelingProtocol
  , codeRequestCategory
  , description
  , url
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idLabelingProtocol
  , OLD.labelingProtocol
  , OLD.codeRequestCategory
  , OLD.description
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For labelingreactionsize 
--

CREATE TABLE IF NOT EXISTS `labelingreactionsize_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeLabelingReactionSize`  varchar(20)  NULL DEFAULT NULL
 ,`labelingReactionSize`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For labelingreactionsize 
--


CREATE TRIGGER TrAI_labelingreactionsize_FER AFTER INSERT ON labelingreactionsize FOR EACH ROW
BEGIN
  INSERT INTO labelingreactionsize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeLabelingReactionSize
  , NEW.labelingReactionSize
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_labelingreactionsize_FER AFTER UPDATE ON labelingreactionsize FOR EACH ROW
BEGIN
  INSERT INTO labelingreactionsize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeLabelingReactionSize
  , NEW.labelingReactionSize
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_labelingreactionsize_FER AFTER DELETE ON labelingreactionsize FOR EACH ROW
BEGIN
  INSERT INTO labelingreactionsize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeLabelingReactionSize
  , OLD.labelingReactionSize
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For label 
--

CREATE TABLE IF NOT EXISTS `label_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabel`  int(10)  NULL DEFAULT NULL
 ,`label`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For label 
--


CREATE TRIGGER TrAI_label_FER AFTER INSERT ON label FOR EACH ROW
BEGIN
  INSERT INTO label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLabel
  , label
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idLabel
  , NEW.label
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_label_FER AFTER UPDATE ON label FOR EACH ROW
BEGIN
  INSERT INTO label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLabel
  , label
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idLabel
  , NEW.label
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_label_FER AFTER DELETE ON label FOR EACH ROW
BEGIN
  INSERT INTO label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLabel
  , label
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idLabel
  , OLD.label
  , OLD.isActive );
END;
$$


--
-- Audit Table For labmanager 
--

CREATE TABLE IF NOT EXISTS `labmanager_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For labmanager 
--


CREATE TRIGGER TrAI_labmanager_FER AFTER INSERT ON labmanager FOR EACH ROW
BEGIN
  INSERT INTO labmanager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAU_labmanager_FER AFTER UPDATE ON labmanager FOR EACH ROW
BEGIN
  INSERT INTO labmanager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAD_labmanager_FER AFTER DELETE ON labmanager FOR EACH ROW
BEGIN
  INSERT INTO labmanager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For labuser 
--

CREATE TABLE IF NOT EXISTS `labuser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For labuser 
--


CREATE TRIGGER TrAI_labuser_FER AFTER INSERT ON labuser FOR EACH ROW
BEGIN
  INSERT INTO labuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sortOrder
  , NEW.isActive
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAU_labuser_FER AFTER UPDATE ON labuser FOR EACH ROW
BEGIN
  INSERT INTO labuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idLab
  , NEW.idAppUser
  , NEW.sortOrder
  , NEW.isActive
  , NEW.sendUploadAlert );
END;
$$


CREATE TRIGGER TrAD_labuser_FER AFTER DELETE ON labuser FOR EACH ROW
BEGIN
  INSERT INTO labuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sortOrder
  , isActive
  , sendUploadAlert )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sortOrder
  , OLD.isActive
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For lab 
--

CREATE TABLE IF NOT EXISTS `lab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For lab 
--


CREATE TRIGGER TrAI_lab_FER AFTER INSERT ON lab FOR EACH ROW
BEGIN
  INSERT INTO lab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_lab_FER AFTER UPDATE ON lab FOR EACH ROW
BEGIN
  INSERT INTO lab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_lab_FER AFTER DELETE ON lab FOR EACH ROW
BEGIN
  INSERT INTO lab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For metrixobject 
--

CREATE TABLE IF NOT EXISTS `metrixobject_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`id`  int(10)  NULL DEFAULT NULL
 ,`run_id`  varchar(512)  NULL DEFAULT NULL
 ,`object_value`  varbinary(8000)  NULL DEFAULT NULL
 ,`state`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For metrixobject 
--


CREATE TRIGGER TrAI_metrixobject_FER AFTER INSERT ON metrixobject FOR EACH ROW
BEGIN
  INSERT INTO metrixobject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.id
  , NEW.run_id
  , NEW.object_value
  , NEW.state );
END;
$$


CREATE TRIGGER TrAU_metrixobject_FER AFTER UPDATE ON metrixobject FOR EACH ROW
BEGIN
  INSERT INTO metrixobject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.id
  , NEW.run_id
  , NEW.object_value
  , NEW.state );
END;
$$


CREATE TRIGGER TrAD_metrixobject_FER AFTER DELETE ON metrixobject FOR EACH ROW
BEGIN
  INSERT INTO metrixobject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.id
  , OLD.run_id
  , OLD.object_value
  , OLD.state );
END;
$$


--
-- Audit Table For newsitem 
--

CREATE TABLE IF NOT EXISTS `newsitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNewsItem`  int(10)  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`title`  varchar(200)  NULL DEFAULT NULL
 ,`message`  varchar(4000)  NULL DEFAULT NULL
 ,`date`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For newsitem 
--


CREATE TRIGGER TrAI_newsitem_FER AFTER INSERT ON newsitem FOR EACH ROW
BEGIN
  INSERT INTO newsitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idNewsItem
  , NEW.idSubmitter
  , NEW.idCoreFacility
  , NEW.title
  , NEW.message
  , NEW.date );
END;
$$


CREATE TRIGGER TrAU_newsitem_FER AFTER UPDATE ON newsitem FOR EACH ROW
BEGIN
  INSERT INTO newsitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idNewsItem
  , NEW.idSubmitter
  , NEW.idCoreFacility
  , NEW.title
  , NEW.message
  , NEW.date );
END;
$$


CREATE TRIGGER TrAD_newsitem_FER AFTER DELETE ON newsitem FOR EACH ROW
BEGIN
  INSERT INTO newsitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idNewsItem
  , idSubmitter
  , idCoreFacility
  , title
  , message
  , date )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idNewsItem
  , OLD.idSubmitter
  , OLD.idCoreFacility
  , OLD.title
  , OLD.message
  , OLD.date );
END;
$$


--
-- Audit Table For notification 
--

CREATE TABLE IF NOT EXISTS `notification_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For notification 
--


CREATE TRIGGER TrAI_notification_FER AFTER INSERT ON notification FOR EACH ROW
BEGIN
  INSERT INTO notification_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_notification_FER AFTER UPDATE ON notification FOR EACH ROW
BEGIN
  INSERT INTO notification_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_notification_FER AFTER DELETE ON notification FOR EACH ROW
BEGIN
  INSERT INTO notification_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For nucleotidetype 
--

CREATE TABLE IF NOT EXISTS `nucleotidetype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For nucleotidetype 
--


CREATE TRIGGER TrAI_nucleotidetype_FER AFTER INSERT ON nucleotidetype FOR EACH ROW
BEGIN
  INSERT INTO nucleotidetype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeNucleotideType )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeNucleotideType );
END;
$$


CREATE TRIGGER TrAU_nucleotidetype_FER AFTER UPDATE ON nucleotidetype FOR EACH ROW
BEGIN
  INSERT INTO nucleotidetype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeNucleotideType )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeNucleotideType );
END;
$$


CREATE TRIGGER TrAD_nucleotidetype_FER AFTER DELETE ON nucleotidetype FOR EACH ROW
BEGIN
  INSERT INTO nucleotidetype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeNucleotideType )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeNucleotideType );
END;
$$


--
-- Audit Table For numbersequencingcyclesallowed 
--

CREATE TABLE IF NOT EXISTS `numbersequencingcyclesallowed_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`protocolDescription`  longtext  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For numbersequencingcyclesallowed 
--


CREATE TRIGGER TrAI_numbersequencingcyclesallowed_FER AFTER INSERT ON numbersequencingcyclesallowed FOR EACH ROW
BEGIN
  INSERT INTO numbersequencingcyclesallowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_numbersequencingcyclesallowed_FER AFTER UPDATE ON numbersequencingcyclesallowed FOR EACH ROW
BEGIN
  INSERT INTO numbersequencingcyclesallowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_numbersequencingcyclesallowed_FER AFTER DELETE ON numbersequencingcyclesallowed FOR EACH ROW
BEGIN
  INSERT INTO numbersequencingcyclesallowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For numbersequencingcycles 
--

CREATE TABLE IF NOT EXISTS `numbersequencingcycles_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`numberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For numbersequencingcycles 
--


CREATE TRIGGER TrAI_numbersequencingcycles_FER AFTER INSERT ON numbersequencingcycles FOR EACH ROW
BEGIN
  INSERT INTO numbersequencingcycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idNumberSequencingCycles
  , NEW.numberSequencingCycles
  , NEW.isActive
  , NEW.sortOrder
  , NEW.notes );
END;
$$


CREATE TRIGGER TrAU_numbersequencingcycles_FER AFTER UPDATE ON numbersequencingcycles FOR EACH ROW
BEGIN
  INSERT INTO numbersequencingcycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idNumberSequencingCycles
  , NEW.numberSequencingCycles
  , NEW.isActive
  , NEW.sortOrder
  , NEW.notes );
END;
$$


CREATE TRIGGER TrAD_numbersequencingcycles_FER AFTER DELETE ON numbersequencingcycles FOR EACH ROW
BEGIN
  INSERT INTO numbersequencingcycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idNumberSequencingCycles
  , numberSequencingCycles
  , isActive
  , sortOrder
  , notes )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idNumberSequencingCycles
  , OLD.numberSequencingCycles
  , OLD.isActive
  , OLD.sortOrder
  , OLD.notes );
END;
$$


--
-- Audit Table For oligobarcodeschemeallowed 
--

CREATE TABLE IF NOT EXISTS `oligobarcodeschemeallowed_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcodeSchemeAllowed`  int(10)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`isIndexGroupB`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For oligobarcodeschemeallowed 
--


CREATE TRIGGER TrAI_oligobarcodeschemeallowed_FER AFTER INSERT ON oligobarcodeschemeallowed FOR EACH ROW
BEGIN
  INSERT INTO oligobarcodeschemeallowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idOligoBarcodeSchemeAllowed
  , NEW.idOligoBarcodeScheme
  , NEW.idSeqLibProtocol
  , NEW.isIndexGroupB );
END;
$$


CREATE TRIGGER TrAU_oligobarcodeschemeallowed_FER AFTER UPDATE ON oligobarcodeschemeallowed FOR EACH ROW
BEGIN
  INSERT INTO oligobarcodeschemeallowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idOligoBarcodeSchemeAllowed
  , NEW.idOligoBarcodeScheme
  , NEW.idSeqLibProtocol
  , NEW.isIndexGroupB );
END;
$$


CREATE TRIGGER TrAD_oligobarcodeschemeallowed_FER AFTER DELETE ON oligobarcodeschemeallowed FOR EACH ROW
BEGIN
  INSERT INTO oligobarcodeschemeallowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idOligoBarcodeSchemeAllowed
  , OLD.idOligoBarcodeScheme
  , OLD.idSeqLibProtocol
  , OLD.isIndexGroupB );
END;
$$


--
-- Audit Table For oligobarcodescheme 
--

CREATE TABLE IF NOT EXISTS `oligobarcodescheme_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`oligoBarcodeScheme`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For oligobarcodescheme 
--


CREATE TRIGGER TrAI_oligobarcodescheme_FER AFTER INSERT ON oligobarcodescheme FOR EACH ROW
BEGIN
  INSERT INTO oligobarcodescheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idOligoBarcodeScheme
  , NEW.oligoBarcodeScheme
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_oligobarcodescheme_FER AFTER UPDATE ON oligobarcodescheme FOR EACH ROW
BEGIN
  INSERT INTO oligobarcodescheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idOligoBarcodeScheme
  , NEW.oligoBarcodeScheme
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_oligobarcodescheme_FER AFTER DELETE ON oligobarcodescheme FOR EACH ROW
BEGIN
  INSERT INTO oligobarcodescheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idOligoBarcodeScheme
  , OLD.oligoBarcodeScheme
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For oligobarcode 
--

CREATE TABLE IF NOT EXISTS `oligobarcode_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcode`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`barcodeSequence`  varchar(20)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For oligobarcode 
--


CREATE TRIGGER TrAI_oligobarcode_FER AFTER INSERT ON oligobarcode FOR EACH ROW
BEGIN
  INSERT INTO oligobarcode_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idOligoBarcode
  , NEW.name
  , NEW.barcodeSequence
  , NEW.idOligoBarcodeScheme
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_oligobarcode_FER AFTER UPDATE ON oligobarcode FOR EACH ROW
BEGIN
  INSERT INTO oligobarcode_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idOligoBarcode
  , NEW.name
  , NEW.barcodeSequence
  , NEW.idOligoBarcodeScheme
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_oligobarcode_FER AFTER DELETE ON oligobarcode FOR EACH ROW
BEGIN
  INSERT INTO oligobarcode_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcode
  , name
  , barcodeSequence
  , idOligoBarcodeScheme
  , sortOrder
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idOligoBarcode
  , OLD.name
  , OLD.barcodeSequence
  , OLD.idOligoBarcodeScheme
  , OLD.sortOrder
  , OLD.isActive );
END;
$$


--
-- Audit Table For organism 
--

CREATE TABLE IF NOT EXISTS `organism_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For organism 
--


CREATE TRIGGER TrAI_organism_FER AFTER INSERT ON organism FOR EACH ROW
BEGIN
  INSERT INTO organism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_organism_FER AFTER UPDATE ON organism FOR EACH ROW
BEGIN
  INSERT INTO organism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_organism_FER AFTER DELETE ON organism FOR EACH ROW
BEGIN
  INSERT INTO organism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For otheraccountfieldsconfiguration 
--

CREATE TABLE IF NOT EXISTS `otheraccountfieldsconfiguration_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOtherAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For otheraccountfieldsconfiguration 
--


CREATE TRIGGER TrAI_otheraccountfieldsconfiguration_FER AFTER INSERT ON otheraccountfieldsconfiguration FOR EACH ROW
BEGIN
  INSERT INTO otheraccountfieldsconfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idOtherAccountFieldsConfiguration
  , NEW.fieldName
  , NEW.include
  , NEW.isRequired );
END;
$$


CREATE TRIGGER TrAU_otheraccountfieldsconfiguration_FER AFTER UPDATE ON otheraccountfieldsconfiguration FOR EACH ROW
BEGIN
  INSERT INTO otheraccountfieldsconfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idOtherAccountFieldsConfiguration
  , NEW.fieldName
  , NEW.include
  , NEW.isRequired );
END;
$$


CREATE TRIGGER TrAD_otheraccountfieldsconfiguration_FER AFTER DELETE ON otheraccountfieldsconfiguration FOR EACH ROW
BEGIN
  INSERT INTO otheraccountfieldsconfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idOtherAccountFieldsConfiguration
  , OLD.fieldName
  , OLD.include
  , OLD.isRequired );
END;
$$


--
-- Audit Table For platetype 
--

CREATE TABLE IF NOT EXISTS `platetype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codePlateType`  varchar(10)  NULL DEFAULT NULL
 ,`plateTypeDescription`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For platetype 
--


CREATE TRIGGER TrAI_platetype_FER AFTER INSERT ON platetype FOR EACH ROW
BEGIN
  INSERT INTO platetype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codePlateType
  , NEW.plateTypeDescription
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_platetype_FER AFTER UPDATE ON platetype FOR EACH ROW
BEGIN
  INSERT INTO platetype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codePlateType
  , NEW.plateTypeDescription
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_platetype_FER AFTER DELETE ON platetype FOR EACH ROW
BEGIN
  INSERT INTO platetype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codePlateType
  , OLD.plateTypeDescription
  , OLD.isActive );
END;
$$


--
-- Audit Table For platewell 
--

CREATE TABLE IF NOT EXISTS `platewell_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For platewell 
--


CREATE TRIGGER TrAI_platewell_FER AFTER INSERT ON platewell FOR EACH ROW
BEGIN
  INSERT INTO platewell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_platewell_FER AFTER UPDATE ON platewell FOR EACH ROW
BEGIN
  INSERT INTO platewell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_platewell_FER AFTER DELETE ON platewell FOR EACH ROW
BEGIN
  INSERT INTO platewell_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For plate 
--

CREATE TABLE IF NOT EXISTS `plate_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For plate 
--


CREATE TRIGGER TrAI_plate_FER AFTER INSERT ON plate FOR EACH ROW
BEGIN
  INSERT INTO plate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_plate_FER AFTER UPDATE ON plate FOR EACH ROW
BEGIN
  INSERT INTO plate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_plate_FER AFTER DELETE ON plate FOR EACH ROW
BEGIN
  INSERT INTO plate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For pricecategorystep 
--

CREATE TABLE IF NOT EXISTS `pricecategorystep_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For pricecategorystep 
--


CREATE TRIGGER TrAI_pricecategorystep_FER AFTER INSERT ON pricecategorystep FOR EACH ROW
BEGIN
  INSERT INTO pricecategorystep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCategory
  , codeStep )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceCategory
  , NEW.codeStep );
END;
$$


CREATE TRIGGER TrAU_pricecategorystep_FER AFTER UPDATE ON pricecategorystep FOR EACH ROW
BEGIN
  INSERT INTO pricecategorystep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCategory
  , codeStep )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceCategory
  , NEW.codeStep );
END;
$$


CREATE TRIGGER TrAD_pricecategorystep_FER AFTER DELETE ON pricecategorystep FOR EACH ROW
BEGIN
  INSERT INTO pricecategorystep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCategory
  , codeStep )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPriceCategory
  , OLD.codeStep );
END;
$$


--
-- Audit Table For pricecategory 
--

CREATE TABLE IF NOT EXISTS `pricecategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`pluginClassName`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter1`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter2`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For pricecategory 
--


CREATE TRIGGER TrAI_pricecategory_FER AFTER INSERT ON pricecategory FOR EACH ROW
BEGIN
  INSERT INTO pricecategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_pricecategory_FER AFTER UPDATE ON pricecategory FOR EACH ROW
BEGIN
  INSERT INTO pricecategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_pricecategory_FER AFTER DELETE ON pricecategory FOR EACH ROW
BEGIN
  INSERT INTO pricecategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCategory
  , name
  , description
  , codeBillingChargeKind
  , pluginClassName
  , dictionaryClassNameFilter1
  , dictionaryClassNameFilter2
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For pricecriteria 
--

CREATE TABLE IF NOT EXISTS `pricecriteria_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCriteria`  int(10)  NULL DEFAULT NULL
 ,`filter1`  varchar(10)  NULL DEFAULT NULL
 ,`filter2`  varchar(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For pricecriteria 
--


CREATE TRIGGER TrAI_pricecriteria_FER AFTER INSERT ON pricecriteria FOR EACH ROW
BEGIN
  INSERT INTO pricecriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceCriteria
  , NEW.filter1
  , NEW.filter2
  , NEW.idPrice );
END;
$$


CREATE TRIGGER TrAU_pricecriteria_FER AFTER UPDATE ON pricecriteria FOR EACH ROW
BEGIN
  INSERT INTO pricecriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceCriteria
  , NEW.filter1
  , NEW.filter2
  , NEW.idPrice );
END;
$$


CREATE TRIGGER TrAD_pricecriteria_FER AFTER DELETE ON pricecriteria FOR EACH ROW
BEGIN
  INSERT INTO pricecriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPriceCriteria
  , OLD.filter1
  , OLD.filter2
  , OLD.idPrice );
END;
$$


--
-- Audit Table For pricesheetpricecategory 
--

CREATE TABLE IF NOT EXISTS `pricesheetpricecategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For pricesheetpricecategory 
--


CREATE TRIGGER TrAI_pricesheetpricecategory_FER AFTER INSERT ON pricesheetpricecategory FOR EACH ROW
BEGIN
  INSERT INTO pricesheetpricecategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceSheet
  , NEW.idPriceCategory
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_pricesheetpricecategory_FER AFTER UPDATE ON pricesheetpricecategory FOR EACH ROW
BEGIN
  INSERT INTO pricesheetpricecategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceSheet
  , NEW.idPriceCategory
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_pricesheetpricecategory_FER AFTER DELETE ON pricesheetpricecategory FOR EACH ROW
BEGIN
  INSERT INTO pricesheetpricecategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPriceSheet
  , OLD.idPriceCategory
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For pricesheetrequestcategory 
--

CREATE TABLE IF NOT EXISTS `pricesheetrequestcategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For pricesheetrequestcategory 
--


CREATE TRIGGER TrAI_pricesheetrequestcategory_FER AFTER INSERT ON pricesheetrequestcategory FOR EACH ROW
BEGIN
  INSERT INTO pricesheetrequestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceSheet
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_pricesheetrequestcategory_FER AFTER UPDATE ON pricesheetrequestcategory FOR EACH ROW
BEGIN
  INSERT INTO pricesheetrequestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceSheet
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_pricesheetrequestcategory_FER AFTER DELETE ON pricesheetrequestcategory FOR EACH ROW
BEGIN
  INSERT INTO pricesheetrequestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPriceSheet
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For pricesheet 
--

CREATE TABLE IF NOT EXISTS `pricesheet_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For pricesheet 
--


CREATE TRIGGER TrAI_pricesheet_FER AFTER INSERT ON pricesheet FOR EACH ROW
BEGIN
  INSERT INTO pricesheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceSheet
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_pricesheet_FER AFTER UPDATE ON pricesheet FOR EACH ROW
BEGIN
  INSERT INTO pricesheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPriceSheet
  , NEW.name
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_pricesheet_FER AFTER DELETE ON pricesheet FOR EACH ROW
BEGIN
  INSERT INTO pricesheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPriceSheet
  , OLD.name
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For price 
--

CREATE TABLE IF NOT EXISTS `price_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`unitPriceExternalAcademic`  decimal(7,2)  NULL DEFAULT NULL
 ,`unitPriceExternalCommercial`  decimal(7,2)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For price 
--


CREATE TRIGGER TrAI_price_FER AFTER INSERT ON price FOR EACH ROW
BEGIN
  INSERT INTO price_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_price_FER AFTER UPDATE ON price FOR EACH ROW
BEGIN
  INSERT INTO price_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_price_FER AFTER DELETE ON price FOR EACH ROW
BEGIN
  INSERT INTO price_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPrice
  , name
  , description
  , unitPrice
  , unitPriceExternalAcademic
  , unitPriceExternalCommercial
  , idPriceCategory
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For primer 
--

CREATE TABLE IF NOT EXISTS `primer_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPrimer`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`sequence`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For primer 
--


CREATE TRIGGER TrAI_primer_FER AFTER INSERT ON primer FOR EACH ROW
BEGIN
  INSERT INTO primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPrimer
  , name
  , description
  , sequence
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPrimer
  , NEW.name
  , NEW.description
  , NEW.sequence
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_primer_FER AFTER UPDATE ON primer FOR EACH ROW
BEGIN
  INSERT INTO primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPrimer
  , name
  , description
  , sequence
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPrimer
  , NEW.name
  , NEW.description
  , NEW.sequence
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_primer_FER AFTER DELETE ON primer FOR EACH ROW
BEGIN
  INSERT INTO primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPrimer
  , name
  , description
  , sequence
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPrimer
  , OLD.name
  , OLD.description
  , OLD.sequence
  , OLD.isActive );
END;
$$


--
-- Audit Table For productledger 
--

CREATE TABLE IF NOT EXISTS `productledger_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductLedger`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`comment`  varchar(5000)  NULL DEFAULT NULL
 ,`timeStame`  datetime  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For productledger 
--


CREATE TRIGGER TrAI_productledger_FER AFTER INSERT ON productledger FOR EACH ROW
BEGIN
  INSERT INTO productledger_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductLedger
  , NEW.idLab
  , NEW.idProduct
  , NEW.qty
  , NEW.comment
  , NEW.timeStame
  , NEW.idProductOrder
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAU_productledger_FER AFTER UPDATE ON productledger FOR EACH ROW
BEGIN
  INSERT INTO productledger_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductLedger
  , NEW.idLab
  , NEW.idProduct
  , NEW.qty
  , NEW.comment
  , NEW.timeStame
  , NEW.idProductOrder
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAD_productledger_FER AFTER DELETE ON productledger FOR EACH ROW
BEGIN
  INSERT INTO productledger_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , idRequest )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProductLedger
  , OLD.idLab
  , OLD.idProduct
  , OLD.qty
  , OLD.comment
  , OLD.timeStame
  , OLD.idProductOrder
  , OLD.idRequest );
END;
$$


--
-- Audit Table For productlineitem 
--

CREATE TABLE IF NOT EXISTS `productlineitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductLineItem`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`codeProductOrderStatus`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For productlineitem 
--


CREATE TRIGGER TrAI_productlineitem_FER AFTER INSERT ON productlineitem FOR EACH ROW
BEGIN
  INSERT INTO productlineitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductLineItem
  , NEW.idProductOrder
  , NEW.idProduct
  , NEW.qty
  , NEW.unitPrice
  , NEW.codeProductOrderStatus );
END;
$$


CREATE TRIGGER TrAU_productlineitem_FER AFTER UPDATE ON productlineitem FOR EACH ROW
BEGIN
  INSERT INTO productlineitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductLineItem
  , NEW.idProductOrder
  , NEW.idProduct
  , NEW.qty
  , NEW.unitPrice
  , NEW.codeProductOrderStatus );
END;
$$


CREATE TRIGGER TrAD_productlineitem_FER AFTER DELETE ON productlineitem FOR EACH ROW
BEGIN
  INSERT INTO productlineitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductLineItem
  , idProductOrder
  , idProduct
  , qty
  , unitPrice
  , codeProductOrderStatus )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProductLineItem
  , OLD.idProductOrder
  , OLD.idProduct
  , OLD.qty
  , OLD.unitPrice
  , OLD.codeProductOrderStatus );
END;
$$


--
-- Audit Table For productorderfile 
--

CREATE TABLE IF NOT EXISTS `productorderfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductOrderFile`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For productorderfile 
--


CREATE TRIGGER TrAI_productorderfile_FER AFTER INSERT ON productorderfile FOR EACH ROW
BEGIN
  INSERT INTO productorderfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductOrderFile
  , NEW.idProductOrder
  , NEW.fileName
  , NEW.fileSize
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAU_productorderfile_FER AFTER UPDATE ON productorderfile FOR EACH ROW
BEGIN
  INSERT INTO productorderfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductOrderFile
  , NEW.idProductOrder
  , NEW.fileName
  , NEW.fileSize
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAD_productorderfile_FER AFTER DELETE ON productorderfile FOR EACH ROW
BEGIN
  INSERT INTO productorderfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProductOrderFile
  , OLD.idProductOrder
  , OLD.fileName
  , OLD.fileSize
  , OLD.createDate );
END;
$$


--
-- Audit Table For productorderstatus 
--

CREATE TABLE IF NOT EXISTS `productorderstatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeProductOrderStatus`  varchar(10)  NULL DEFAULT NULL
 ,`productOrderStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For productorderstatus 
--


CREATE TRIGGER TrAI_productorderstatus_FER AFTER INSERT ON productorderstatus FOR EACH ROW
BEGIN
  INSERT INTO productorderstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeProductOrderStatus
  , NEW.productOrderStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_productorderstatus_FER AFTER UPDATE ON productorderstatus FOR EACH ROW
BEGIN
  INSERT INTO productorderstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeProductOrderStatus
  , NEW.productOrderStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_productorderstatus_FER AFTER DELETE ON productorderstatus FOR EACH ROW
BEGIN
  INSERT INTO productorderstatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeProductOrderStatus
  , OLD.productOrderStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For productorder 
--

CREATE TABLE IF NOT EXISTS `productorder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`submitDate`  datetime  NULL DEFAULT NULL
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`quoteNumber`  varchar(50)  NULL DEFAULT NULL
 ,`quoteReceivedDate`  datetime  NULL DEFAULT NULL
 ,`uuid`  varchar(36)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`productOrderNumber`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For productorder 
--


CREATE TRIGGER TrAI_productorder_FER AFTER INSERT ON productorder FOR EACH ROW
BEGIN
  INSERT INTO productorder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , codeProductType
  , quoteNumber
  , quoteReceivedDate
  , uuid
  , idBillingAccount
  , productOrderNumber )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductOrder
  , NEW.idAppUser
  , NEW.idLab
  , NEW.idCoreFacility
  , NEW.submitDate
  , NEW.codeProductType
  , NEW.quoteNumber
  , NEW.quoteReceivedDate
  , NEW.uuid
  , NEW.idBillingAccount
  , NEW.productOrderNumber );
END;
$$


CREATE TRIGGER TrAU_productorder_FER AFTER UPDATE ON productorder FOR EACH ROW
BEGIN
  INSERT INTO productorder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , codeProductType
  , quoteNumber
  , quoteReceivedDate
  , uuid
  , idBillingAccount
  , productOrderNumber )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProductOrder
  , NEW.idAppUser
  , NEW.idLab
  , NEW.idCoreFacility
  , NEW.submitDate
  , NEW.codeProductType
  , NEW.quoteNumber
  , NEW.quoteReceivedDate
  , NEW.uuid
  , NEW.idBillingAccount
  , NEW.productOrderNumber );
END;
$$


CREATE TRIGGER TrAD_productorder_FER AFTER DELETE ON productorder FOR EACH ROW
BEGIN
  INSERT INTO productorder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductOrder
  , idAppUser
  , idLab
  , idCoreFacility
  , submitDate
  , codeProductType
  , quoteNumber
  , quoteReceivedDate
  , uuid
  , idBillingAccount
  , productOrderNumber )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProductOrder
  , OLD.idAppUser
  , OLD.idLab
  , OLD.idCoreFacility
  , OLD.submitDate
  , OLD.codeProductType
  , OLD.quoteNumber
  , OLD.quoteReceivedDate
  , OLD.uuid
  , OLD.idBillingAccount
  , OLD.productOrderNumber );
END;
$$


--
-- Audit Table For producttype 
--

CREATE TABLE IF NOT EXISTS `producttype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For producttype 
--


CREATE TRIGGER TrAI_producttype_FER AFTER INSERT ON producttype FOR EACH ROW
BEGIN
  INSERT INTO producttype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeProductType
  , NEW.description
  , NEW.idCoreFacility
  , NEW.idVendor
  , NEW.idPriceCategory );
END;
$$


CREATE TRIGGER TrAU_producttype_FER AFTER UPDATE ON producttype FOR EACH ROW
BEGIN
  INSERT INTO producttype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeProductType
  , NEW.description
  , NEW.idCoreFacility
  , NEW.idVendor
  , NEW.idPriceCategory );
END;
$$


CREATE TRIGGER TrAD_producttype_FER AFTER DELETE ON producttype FOR EACH ROW
BEGIN
  INSERT INTO producttype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeProductType
  , OLD.description
  , OLD.idCoreFacility
  , OLD.idVendor
  , OLD.idPriceCategory );
END;
$$


--
-- Audit Table For product 
--

CREATE TABLE IF NOT EXISTS `product_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`orderQty`  int(10)  NULL DEFAULT NULL
 ,`useQty`  int(10)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`batchSamplesByUseQuantity`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For product 
--


CREATE TRIGGER TrAI_product_FER AFTER INSERT ON product FOR EACH ROW
BEGIN
  INSERT INTO product_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProduct
  , name
  , codeProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProduct
  , NEW.name
  , NEW.codeProductType
  , NEW.idPrice
  , NEW.orderQty
  , NEW.useQty
  , NEW.catalogNumber
  , NEW.isActive
  , NEW.batchSamplesByUseQuantity );
END;
$$


CREATE TRIGGER TrAU_product_FER AFTER UPDATE ON product FOR EACH ROW
BEGIN
  INSERT INTO product_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProduct
  , name
  , codeProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProduct
  , NEW.name
  , NEW.codeProductType
  , NEW.idPrice
  , NEW.orderQty
  , NEW.useQty
  , NEW.catalogNumber
  , NEW.isActive
  , NEW.batchSamplesByUseQuantity );
END;
$$


CREATE TRIGGER TrAD_product_FER AFTER DELETE ON product FOR EACH ROW
BEGIN
  INSERT INTO product_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProduct
  , name
  , codeProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
  , batchSamplesByUseQuantity )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProduct
  , OLD.name
  , OLD.codeProductType
  , OLD.idPrice
  , OLD.orderQty
  , OLD.useQty
  , OLD.catalogNumber
  , OLD.isActive
  , OLD.batchSamplesByUseQuantity );
END;
$$


--
-- Audit Table For project 
--

CREATE TABLE IF NOT EXISTS `project_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(4000)  NULL DEFAULT NULL
 ,`publicDateForAppUsers`  datetime  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For project 
--


CREATE TRIGGER TrAI_project_FER AFTER INSERT ON project FOR EACH ROW
BEGIN
  INSERT INTO project_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProject
  , NEW.name
  , NEW.description
  , NEW.publicDateForAppUsers
  , NEW.idLab
  , NEW.idAppUser
  , NEW.codeVisibility );
END;
$$


CREATE TRIGGER TrAU_project_FER AFTER UPDATE ON project FOR EACH ROW
BEGIN
  INSERT INTO project_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProject
  , NEW.name
  , NEW.description
  , NEW.publicDateForAppUsers
  , NEW.idLab
  , NEW.idAppUser
  , NEW.codeVisibility );
END;
$$


CREATE TRIGGER TrAD_project_FER AFTER DELETE ON project FOR EACH ROW
BEGIN
  INSERT INTO project_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProject
  , name
  , description
  , publicDateForAppUsers
  , idLab
  , idAppUser
  , codeVisibility )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For propertyanalysistype 
--

CREATE TABLE IF NOT EXISTS `propertyanalysistype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyanalysistype 
--


CREATE TRIGGER TrAI_propertyanalysistype_FER AFTER INSERT ON propertyanalysistype FOR EACH ROW
BEGIN
  INSERT INTO propertyanalysistype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idAnalysisType )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProperty
  , NEW.idAnalysisType );
END;
$$


CREATE TRIGGER TrAU_propertyanalysistype_FER AFTER UPDATE ON propertyanalysistype FOR EACH ROW
BEGIN
  INSERT INTO propertyanalysistype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idAnalysisType )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProperty
  , NEW.idAnalysisType );
END;
$$


CREATE TRIGGER TrAD_propertyanalysistype_FER AFTER DELETE ON propertyanalysistype FOR EACH ROW
BEGIN
  INSERT INTO propertyanalysistype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idAnalysisType )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProperty
  , OLD.idAnalysisType );
END;
$$


--
-- Audit Table For propertyappuser 
--

CREATE TABLE IF NOT EXISTS `propertyappuser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyappuser 
--


CREATE TRIGGER TrAI_propertyappuser_FER AFTER INSERT ON propertyappuser FOR EACH ROW
BEGIN
  INSERT INTO propertyappuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProperty
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_propertyappuser_FER AFTER UPDATE ON propertyappuser FOR EACH ROW
BEGIN
  INSERT INTO propertyappuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProperty
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_propertyappuser_FER AFTER DELETE ON propertyappuser FOR EACH ROW
BEGIN
  INSERT INTO propertyappuser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProperty
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For propertydictionary 
--

CREATE TABLE IF NOT EXISTS `propertydictionary_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyDictionary`  int(10)  NULL DEFAULT NULL
 ,`propertyName`  varchar(200)  NULL DEFAULT NULL
 ,`propertyValue`  varchar(2000)  NULL DEFAULT NULL
 ,`propertyDescription`  varchar(2000)  NULL DEFAULT NULL
 ,`forServerOnly`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertydictionary 
--


CREATE TRIGGER TrAI_propertydictionary_FER AFTER INSERT ON propertydictionary FOR EACH ROW
BEGIN
  INSERT INTO propertydictionary_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyDictionary
  , NEW.propertyName
  , NEW.propertyValue
  , NEW.propertyDescription
  , NEW.forServerOnly
  , NEW.idCoreFacility
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_propertydictionary_FER AFTER UPDATE ON propertydictionary FOR EACH ROW
BEGIN
  INSERT INTO propertydictionary_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyDictionary
  , NEW.propertyName
  , NEW.propertyValue
  , NEW.propertyDescription
  , NEW.forServerOnly
  , NEW.idCoreFacility
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_propertydictionary_FER AFTER DELETE ON propertydictionary FOR EACH ROW
BEGIN
  INSERT INTO propertydictionary_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyDictionary
  , propertyName
  , propertyValue
  , propertyDescription
  , forServerOnly
  , idCoreFacility
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For propertyentryoption 
--

CREATE TABLE IF NOT EXISTS `propertyentryoption_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyentryoption 
--


CREATE TRIGGER TrAI_propertyentryoption_FER AFTER INSERT ON propertyentryoption FOR EACH ROW
BEGIN
  INSERT INTO propertyentryoption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyEntry
  , NEW.idPropertyOption );
END;
$$


CREATE TRIGGER TrAU_propertyentryoption_FER AFTER UPDATE ON propertyentryoption FOR EACH ROW
BEGIN
  INSERT INTO propertyentryoption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyEntry
  , NEW.idPropertyOption );
END;
$$


CREATE TRIGGER TrAD_propertyentryoption_FER AFTER DELETE ON propertyentryoption FOR EACH ROW
BEGIN
  INSERT INTO propertyentryoption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPropertyEntry
  , OLD.idPropertyOption );
END;
$$


--
-- Audit Table For propertyentryvalue 
--

CREATE TABLE IF NOT EXISTS `propertyentryvalue_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntryValue`  int(10) unsigned  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyentryvalue 
--


CREATE TRIGGER TrAI_propertyentryvalue_FER AFTER INSERT ON propertyentryvalue FOR EACH ROW
BEGIN
  INSERT INTO propertyentryvalue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyEntryValue
  , NEW.value
  , NEW.idPropertyEntry );
END;
$$


CREATE TRIGGER TrAU_propertyentryvalue_FER AFTER UPDATE ON propertyentryvalue FOR EACH ROW
BEGIN
  INSERT INTO propertyentryvalue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyEntryValue
  , NEW.value
  , NEW.idPropertyEntry );
END;
$$


CREATE TRIGGER TrAD_propertyentryvalue_FER AFTER DELETE ON propertyentryvalue FOR EACH ROW
BEGIN
  INSERT INTO propertyentryvalue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPropertyEntryValue
  , OLD.value
  , OLD.idPropertyEntry );
END;
$$


--
-- Audit Table For propertyentry 
--

CREATE TABLE IF NOT EXISTS `propertyentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(200)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyentry 
--


CREATE TRIGGER TrAI_propertyentry_FER AFTER INSERT ON propertyentry FOR EACH ROW
BEGIN
  INSERT INTO propertyentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_propertyentry_FER AFTER UPDATE ON propertyentry FOR EACH ROW
BEGIN
  INSERT INTO propertyentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_propertyentry_FER AFTER DELETE ON propertyentry FOR EACH ROW
BEGIN
  INSERT INTO propertyentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
  , idRequest )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For propertyoption 
--

CREATE TABLE IF NOT EXISTS `propertyoption_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyoption 
--


CREATE TRIGGER TrAI_propertyoption_FER AFTER INSERT ON propertyoption FOR EACH ROW
BEGIN
  INSERT INTO propertyoption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyOption
  , NEW.value
  , NEW.idProperty
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_propertyoption_FER AFTER UPDATE ON propertyoption FOR EACH ROW
BEGIN
  INSERT INTO propertyoption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPropertyOption
  , NEW.value
  , NEW.idProperty
  , NEW.sortOrder
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_propertyoption_FER AFTER DELETE ON propertyoption FOR EACH ROW
BEGIN
  INSERT INTO propertyoption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyOption
  , value
  , idProperty
  , sortOrder
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPropertyOption
  , OLD.value
  , OLD.idProperty
  , OLD.sortOrder
  , OLD.isActive );
END;
$$


--
-- Audit Table For propertyorganism 
--

CREATE TABLE IF NOT EXISTS `propertyorganism_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyorganism 
--


CREATE TRIGGER TrAI_propertyorganism_FER AFTER INSERT ON propertyorganism FOR EACH ROW
BEGIN
  INSERT INTO propertyorganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idOrganism )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idProperty
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAU_propertyorganism_FER AFTER UPDATE ON propertyorganism FOR EACH ROW
BEGIN
  INSERT INTO propertyorganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idOrganism )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idProperty
  , NEW.idOrganism );
END;
$$


CREATE TRIGGER TrAD_propertyorganism_FER AFTER DELETE ON propertyorganism FOR EACH ROW
BEGIN
  INSERT INTO propertyorganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idOrganism )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idProperty
  , OLD.idOrganism );
END;
$$


--
-- Audit Table For propertyplatformapplication 
--

CREATE TABLE IF NOT EXISTS `propertyplatformapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPlatformApplication`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertyplatformapplication 
--


CREATE TRIGGER TrAI_propertyplatformapplication_FER AFTER INSERT ON propertyplatformapplication FOR EACH ROW
BEGIN
  INSERT INTO propertyplatformapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idPlatformApplication
  , NEW.idProperty
  , NEW.codeRequestCategory
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAU_propertyplatformapplication_FER AFTER UPDATE ON propertyplatformapplication FOR EACH ROW
BEGIN
  INSERT INTO propertyplatformapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idPlatformApplication
  , NEW.idProperty
  , NEW.codeRequestCategory
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAD_propertyplatformapplication_FER AFTER DELETE ON propertyplatformapplication FOR EACH ROW
BEGIN
  INSERT INTO propertyplatformapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idPlatformApplication
  , OLD.idProperty
  , OLD.codeRequestCategory
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For propertytype 
--

CREATE TABLE IF NOT EXISTS `propertytype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codePropertyType`  varchar(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For propertytype 
--


CREATE TRIGGER TrAI_propertytype_FER AFTER INSERT ON propertytype FOR EACH ROW
BEGIN
  INSERT INTO propertytype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codePropertyType
  , NEW.name
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_propertytype_FER AFTER UPDATE ON propertytype FOR EACH ROW
BEGIN
  INSERT INTO propertytype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codePropertyType
  , NEW.name
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_propertytype_FER AFTER DELETE ON propertytype FOR EACH ROW
BEGIN
  INSERT INTO propertytype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codePropertyType
  , OLD.name
  , OLD.isActive );
END;
$$


--
-- Audit Table For property 
--

CREATE TABLE IF NOT EXISTS `property_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For property 
--


CREATE TRIGGER TrAI_property_FER AFTER INSERT ON property FOR EACH ROW
BEGIN
  INSERT INTO property_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , forRequest )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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
  , NEW.forRequest );
END;
$$


CREATE TRIGGER TrAU_property_FER AFTER UPDATE ON property FOR EACH ROW
BEGIN
  INSERT INTO property_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , forRequest )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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
  , NEW.forRequest );
END;
$$


CREATE TRIGGER TrAD_property_FER AFTER DELETE ON property FOR EACH ROW
BEGIN
  INSERT INTO property_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , forRequest )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
  , OLD.forRequest );
END;
$$


--
-- Audit Table For protocoltype 
--

CREATE TABLE IF NOT EXISTS `protocoltype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeProtocolType`  varchar(10)  NULL DEFAULT NULL
 ,`protocolType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For protocoltype 
--


CREATE TRIGGER TrAI_protocoltype_FER AFTER INSERT ON protocoltype FOR EACH ROW
BEGIN
  INSERT INTO protocoltype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeProtocolType
  , NEW.protocolType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_protocoltype_FER AFTER UPDATE ON protocoltype FOR EACH ROW
BEGIN
  INSERT INTO protocoltype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeProtocolType
  , NEW.protocolType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_protocoltype_FER AFTER DELETE ON protocoltype FOR EACH ROW
BEGIN
  INSERT INTO protocoltype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeProtocolType
  , OLD.protocolType
  , OLD.isActive );
END;
$$


--
-- Audit Table For qualitycontrolstepentry 
--

CREATE TABLE IF NOT EXISTS `qualitycontrolstepentry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idQualityControlStepEntry`  int(10)  NULL DEFAULT NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For qualitycontrolstepentry 
--


CREATE TRIGGER TrAI_qualitycontrolstepentry_FER AFTER INSERT ON qualitycontrolstepentry FOR EACH ROW
BEGIN
  INSERT INTO qualitycontrolstepentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idQualityControlStepEntry
  , NEW.codeQualityControlStep
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_qualitycontrolstepentry_FER AFTER UPDATE ON qualitycontrolstepentry FOR EACH ROW
BEGIN
  INSERT INTO qualitycontrolstepentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idQualityControlStepEntry
  , NEW.codeQualityControlStep
  , NEW.idProject
  , NEW.valueString
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_qualitycontrolstepentry_FER AFTER DELETE ON qualitycontrolstepentry FOR EACH ROW
BEGIN
  INSERT INTO qualitycontrolstepentry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idQualityControlStepEntry
  , codeQualityControlStep
  , idProject
  , valueString
  , otherLabel )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idQualityControlStepEntry
  , OLD.codeQualityControlStep
  , OLD.idProject
  , OLD.valueString
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For qualitycontrolstep 
--

CREATE TABLE IF NOT EXISTS `qualitycontrolstep_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`qualityControlStep`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For qualitycontrolstep 
--


CREATE TRIGGER TrAI_qualitycontrolstep_FER AFTER INSERT ON qualitycontrolstep FOR EACH ROW
BEGIN
  INSERT INTO qualitycontrolstep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeQualityControlStep
  , NEW.qualityControlStep
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_qualitycontrolstep_FER AFTER UPDATE ON qualitycontrolstep FOR EACH ROW
BEGIN
  INSERT INTO qualitycontrolstep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeQualityControlStep
  , NEW.qualityControlStep
  , NEW.mageOntologyCode
  , NEW.mageOntologyDefinition
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_qualitycontrolstep_FER AFTER DELETE ON qualitycontrolstep FOR EACH ROW
BEGIN
  INSERT INTO qualitycontrolstep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeQualityControlStep
  , qualityControlStep
  , mageOntologyCode
  , mageOntologyDefinition
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeQualityControlStep
  , OLD.qualityControlStep
  , OLD.mageOntologyCode
  , OLD.mageOntologyDefinition
  , OLD.isActive );
END;
$$


--
-- Audit Table For reactiontype 
--

CREATE TABLE IF NOT EXISTS `reactiontype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`reactionType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For reactiontype 
--


CREATE TRIGGER TrAI_reactiontype_FER AFTER INSERT ON reactiontype FOR EACH ROW
BEGIN
  INSERT INTO reactiontype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeReactionType
  , NEW.reactionType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_reactiontype_FER AFTER UPDATE ON reactiontype FOR EACH ROW
BEGIN
  INSERT INTO reactiontype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeReactionType
  , NEW.reactionType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_reactiontype_FER AFTER DELETE ON reactiontype FOR EACH ROW
BEGIN
  INSERT INTO reactiontype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeReactionType
  , OLD.reactionType
  , OLD.isActive );
END;
$$


--
-- Audit Table For requestcategoryapplication 
--

CREATE TABLE IF NOT EXISTS `requestcategoryapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idLabelingProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocolDefault`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requestcategoryapplication 
--


CREATE TRIGGER TrAI_requestcategoryapplication_FER AFTER INSERT ON requestcategoryapplication FOR EACH ROW
BEGIN
  INSERT INTO requestcategoryapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.idLabelingProtocolDefault
  , NEW.idHybProtocolDefault
  , NEW.idScanProtocolDefault
  , NEW.idFeatureExtractionProtocolDefault );
END;
$$


CREATE TRIGGER TrAU_requestcategoryapplication_FER AFTER UPDATE ON requestcategoryapplication FOR EACH ROW
BEGIN
  INSERT INTO requestcategoryapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.idLabelingProtocolDefault
  , NEW.idHybProtocolDefault
  , NEW.idScanProtocolDefault
  , NEW.idFeatureExtractionProtocolDefault );
END;
$$


CREATE TRIGGER TrAD_requestcategoryapplication_FER AFTER DELETE ON requestcategoryapplication FOR EACH ROW
BEGIN
  INSERT INTO requestcategoryapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestCategory
  , codeApplication
  , idLabelingProtocolDefault
  , idHybProtocolDefault
  , idScanProtocolDefault
  , idFeatureExtractionProtocolDefault )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeRequestCategory
  , OLD.codeApplication
  , OLD.idLabelingProtocolDefault
  , OLD.idHybProtocolDefault
  , OLD.idScanProtocolDefault
  , OLD.idFeatureExtractionProtocolDefault );
END;
$$


--
-- Audit Table For requestcategorytype 
--

CREATE TABLE IF NOT EXISTS `requestcategorytype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategoryType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(50)  NULL DEFAULT NULL
 ,`defaultIcon`  varchar(100)  NULL DEFAULT NULL
 ,`isIllumina`  char(1)  NULL DEFAULT NULL
 ,`hasChannels`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requestcategorytype 
--


CREATE TRIGGER TrAI_requestcategorytype_FER AFTER INSERT ON requestcategorytype FOR EACH ROW
BEGIN
  INSERT INTO requestcategorytype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeRequestCategoryType
  , NEW.description
  , NEW.defaultIcon
  , NEW.isIllumina
  , NEW.hasChannels );
END;
$$


CREATE TRIGGER TrAU_requestcategorytype_FER AFTER UPDATE ON requestcategorytype FOR EACH ROW
BEGIN
  INSERT INTO requestcategorytype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeRequestCategoryType
  , NEW.description
  , NEW.defaultIcon
  , NEW.isIllumina
  , NEW.hasChannels );
END;
$$


CREATE TRIGGER TrAD_requestcategorytype_FER AFTER DELETE ON requestcategorytype FOR EACH ROW
BEGIN
  INSERT INTO requestcategorytype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestCategoryType
  , description
  , defaultIcon
  , isIllumina
  , hasChannels )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeRequestCategoryType
  , OLD.description
  , OLD.defaultIcon
  , OLD.isIllumina
  , OLD.hasChannels );
END;
$$


--
-- Audit Table For requestcategory 
--

CREATE TABLE IF NOT EXISTS `requestcategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`associatedWithAnalysis`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requestcategory 
--


CREATE TRIGGER TrAI_requestcategory_FER AFTER INSERT ON requestcategory FOR EACH ROW
BEGIN
  INSERT INTO requestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , codeProductType
  , associatedWithAnalysis )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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
  , NEW.codeProductType
  , NEW.associatedWithAnalysis );
END;
$$


CREATE TRIGGER TrAU_requestcategory_FER AFTER UPDATE ON requestcategory FOR EACH ROW
BEGIN
  INSERT INTO requestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , codeProductType
  , associatedWithAnalysis )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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
  , NEW.codeProductType
  , NEW.associatedWithAnalysis );
END;
$$


CREATE TRIGGER TrAD_requestcategory_FER AFTER DELETE ON requestcategory FOR EACH ROW
BEGIN
  INSERT INTO requestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , codeProductType
  , associatedWithAnalysis )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
  , OLD.codeProductType
  , OLD.associatedWithAnalysis );
END;
$$


--
-- Audit Table For requestcollaborator 
--

CREATE TABLE IF NOT EXISTS `requestcollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requestcollaborator 
--


CREATE TRIGGER TrAI_requestcollaborator_FER AFTER INSERT ON requestcollaborator FOR EACH ROW
BEGIN
  INSERT INTO requestcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idRequest
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAU_requestcollaborator_FER AFTER UPDATE ON requestcollaborator FOR EACH ROW
BEGIN
  INSERT INTO requestcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idRequest
  , NEW.idAppUser
  , NEW.canUploadData
  , NEW.canUpdate );
END;
$$


CREATE TRIGGER TrAD_requestcollaborator_FER AFTER DELETE ON requestcollaborator FOR EACH ROW
BEGIN
  INSERT INTO requestcollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idRequest
  , OLD.idAppUser
  , OLD.canUploadData
  , OLD.canUpdate );
END;
$$


--
-- Audit Table For requesthybridization 
--

CREATE TABLE IF NOT EXISTS `requesthybridization_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requesthybridization 
--


CREATE TRIGGER TrAI_requesthybridization_FER AFTER INSERT ON requesthybridization FOR EACH ROW
BEGIN
  INSERT INTO requesthybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idHybridization )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idRequest
  , NEW.idHybridization );
END;
$$


CREATE TRIGGER TrAU_requesthybridization_FER AFTER UPDATE ON requesthybridization FOR EACH ROW
BEGIN
  INSERT INTO requesthybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idHybridization )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idRequest
  , NEW.idHybridization );
END;
$$


CREATE TRIGGER TrAD_requesthybridization_FER AFTER DELETE ON requesthybridization FOR EACH ROW
BEGIN
  INSERT INTO requesthybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idHybridization )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idRequest
  , OLD.idHybridization );
END;
$$


--
-- Audit Table For requestseqlibtreatment 
--

CREATE TABLE IF NOT EXISTS `requestseqlibtreatment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requestseqlibtreatment 
--


CREATE TRIGGER TrAI_requestseqlibtreatment_FER AFTER INSERT ON requestseqlibtreatment FOR EACH ROW
BEGIN
  INSERT INTO requestseqlibtreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idRequest
  , NEW.idSeqLibTreatment );
END;
$$


CREATE TRIGGER TrAU_requestseqlibtreatment_FER AFTER UPDATE ON requestseqlibtreatment FOR EACH ROW
BEGIN
  INSERT INTO requestseqlibtreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idRequest
  , NEW.idSeqLibTreatment );
END;
$$


CREATE TRIGGER TrAD_requestseqlibtreatment_FER AFTER DELETE ON requestseqlibtreatment FOR EACH ROW
BEGIN
  INSERT INTO requestseqlibtreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idRequest
  , OLD.idSeqLibTreatment );
END;
$$


--
-- Audit Table For requeststatus 
--

CREATE TABLE IF NOT EXISTS `requeststatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestStatus`  varchar(10)  NULL DEFAULT NULL
 ,`requestStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requeststatus 
--


CREATE TRIGGER TrAI_requeststatus_FER AFTER INSERT ON requeststatus FOR EACH ROW
BEGIN
  INSERT INTO requeststatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeRequestStatus
  , NEW.requestStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_requeststatus_FER AFTER UPDATE ON requeststatus FOR EACH ROW
BEGIN
  INSERT INTO requeststatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeRequestStatus
  , NEW.requestStatus
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_requeststatus_FER AFTER DELETE ON requeststatus FOR EACH ROW
BEGIN
  INSERT INTO requeststatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeRequestStatus
  , OLD.requestStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For requesttotopic 
--

CREATE TABLE IF NOT EXISTS `requesttotopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For requesttotopic 
--


CREATE TRIGGER TrAI_requesttotopic_FER AFTER INSERT ON requesttotopic FOR EACH ROW
BEGIN
  INSERT INTO requesttotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idRequest )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idTopic
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAU_requesttotopic_FER AFTER UPDATE ON requesttotopic FOR EACH ROW
BEGIN
  INSERT INTO requesttotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idRequest )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idTopic
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAD_requesttotopic_FER AFTER DELETE ON requesttotopic FOR EACH ROW
BEGIN
  INSERT INTO requesttotopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idRequest )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idTopic
  , OLD.idRequest );
END;
$$


--
-- Audit Table For request 
--

CREATE TABLE IF NOT EXISTS `request_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
 ,`numberIScanChips`  int(10)  NULL DEFAULT NULL
 ,`idIScanChip`  int(10)  NULL DEFAULT NULL
 ,`coreToExtractDNA`  char(1)  NULL DEFAULT NULL
 ,`applicationNotes`  varchar(5000)  NULL DEFAULT NULL
 ,`processingDate`  datetime  NULL DEFAULT NULL
 ,`codeIsolationPrepType`  varchar(15)  NULL DEFAULT NULL
 ,`bioinformaticsAssist`  char(1)  NULL DEFAULT NULL
 ,`hasPrePooledLibraries`  char(1)  NULL DEFAULT NULL
 ,`numPrePooledTubes`  int(10)  NULL DEFAULT NULL
 ,`includeBisulfideConversion`  char(1)  NULL DEFAULT NULL
 ,`includeQubitConcentration`  char(1)  NULL DEFAULT NULL
 ,`alignToGenomeBuild`  char(1)  NULL DEFAULT NULL
 ,`adminNotes`  varchar(5000)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For request 
--


CREATE TRIGGER TrAI_request_FER AFTER INSERT ON request FOR EACH ROW
BEGIN
  INSERT INTO request_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , numberIScanChips
  , idIScanChip
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes
  , idProduct )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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
  , NEW.numberIScanChips
  , NEW.idIScanChip
  , NEW.coreToExtractDNA
  , NEW.applicationNotes
  , NEW.processingDate
  , NEW.codeIsolationPrepType
  , NEW.bioinformaticsAssist
  , NEW.hasPrePooledLibraries
  , NEW.numPrePooledTubes
  , NEW.includeBisulfideConversion
  , NEW.includeQubitConcentration
  , NEW.alignToGenomeBuild
  , NEW.adminNotes
  , NEW.idProduct );
END;
$$


CREATE TRIGGER TrAU_request_FER AFTER UPDATE ON request FOR EACH ROW
BEGIN
  INSERT INTO request_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , numberIScanChips
  , idIScanChip
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes
  , idProduct )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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
  , NEW.numberIScanChips
  , NEW.idIScanChip
  , NEW.coreToExtractDNA
  , NEW.applicationNotes
  , NEW.processingDate
  , NEW.codeIsolationPrepType
  , NEW.bioinformaticsAssist
  , NEW.hasPrePooledLibraries
  , NEW.numPrePooledTubes
  , NEW.includeBisulfideConversion
  , NEW.includeQubitConcentration
  , NEW.alignToGenomeBuild
  , NEW.adminNotes
  , NEW.idProduct );
END;
$$


CREATE TRIGGER TrAD_request_FER AFTER DELETE ON request FOR EACH ROW
BEGIN
  INSERT INTO request_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , numberIScanChips
  , idIScanChip
  , coreToExtractDNA
  , applicationNotes
  , processingDate
  , codeIsolationPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes
  , idProduct )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
  , OLD.numberIScanChips
  , OLD.idIScanChip
  , OLD.coreToExtractDNA
  , OLD.applicationNotes
  , OLD.processingDate
  , OLD.codeIsolationPrepType
  , OLD.bioinformaticsAssist
  , OLD.hasPrePooledLibraries
  , OLD.numPrePooledTubes
  , OLD.includeBisulfideConversion
  , OLD.includeQubitConcentration
  , OLD.alignToGenomeBuild
  , OLD.adminNotes
  , OLD.idProduct );
END;
$$


--
-- Audit Table For sampledropofflocation 
--

CREATE TABLE IF NOT EXISTS `sampledropofflocation_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleDropOffLocation`  int(10)  NULL DEFAULT NULL
 ,`sampleDropOffLocation`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sampledropofflocation 
--


CREATE TRIGGER TrAI_sampledropofflocation_FER AFTER INSERT ON sampledropofflocation FOR EACH ROW
BEGIN
  INSERT INTO sampledropofflocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleDropOffLocation
  , NEW.sampleDropOffLocation
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_sampledropofflocation_FER AFTER UPDATE ON sampledropofflocation FOR EACH ROW
BEGIN
  INSERT INTO sampledropofflocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleDropOffLocation
  , NEW.sampleDropOffLocation
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_sampledropofflocation_FER AFTER DELETE ON sampledropofflocation FOR EACH ROW
BEGIN
  INSERT INTO sampledropofflocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSampleDropOffLocation
  , OLD.sampleDropOffLocation
  , OLD.isActive );
END;
$$


--
-- Audit Table For sampleexperimentfile 
--

CREATE TABLE IF NOT EXISTS `sampleexperimentfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleExperimentFile`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idExpFileRead1`  int(10)  NULL DEFAULT NULL
 ,`idExpFileRead2`  int(10)  NULL DEFAULT NULL
 ,`seqRunNumber`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sampleexperimentfile 
--


CREATE TRIGGER TrAI_sampleexperimentfile_FER AFTER INSERT ON sampleexperimentfile FOR EACH ROW
BEGIN
  INSERT INTO sampleexperimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleExperimentFile
  , NEW.idSample
  , NEW.idExpFileRead1
  , NEW.idExpFileRead2
  , NEW.seqRunNumber );
END;
$$


CREATE TRIGGER TrAU_sampleexperimentfile_FER AFTER UPDATE ON sampleexperimentfile FOR EACH ROW
BEGIN
  INSERT INTO sampleexperimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleExperimentFile
  , NEW.idSample
  , NEW.idExpFileRead1
  , NEW.idExpFileRead2
  , NEW.seqRunNumber );
END;
$$


CREATE TRIGGER TrAD_sampleexperimentfile_FER AFTER DELETE ON sampleexperimentfile FOR EACH ROW
BEGIN
  INSERT INTO sampleexperimentfile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSampleExperimentFile
  , OLD.idSample
  , OLD.idExpFileRead1
  , OLD.idExpFileRead2
  , OLD.seqRunNumber );
END;
$$


--
-- Audit Table For samplefiletype 
--

CREATE TABLE IF NOT EXISTS `samplefiletype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSampleFileType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For samplefiletype 
--


CREATE TRIGGER TrAI_samplefiletype_FER AFTER INSERT ON samplefiletype FOR EACH ROW
BEGIN
  INSERT INTO samplefiletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSampleFileType
  , description )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSampleFileType
  , NEW.description );
END;
$$


CREATE TRIGGER TrAU_samplefiletype_FER AFTER UPDATE ON samplefiletype FOR EACH ROW
BEGIN
  INSERT INTO samplefiletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSampleFileType
  , description )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSampleFileType
  , NEW.description );
END;
$$


CREATE TRIGGER TrAD_samplefiletype_FER AFTER DELETE ON samplefiletype FOR EACH ROW
BEGIN
  INSERT INTO samplefiletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSampleFileType
  , description )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeSampleFileType
  , OLD.description );
END;
$$


--
-- Audit Table For sampleprepmethod 
--

CREATE TABLE IF NOT EXISTS `sampleprepmethod_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSamplePrepMethod`  int(10)  NULL DEFAULT NULL
 ,`samplePrepMethod`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sampleprepmethod 
--


CREATE TRIGGER TrAI_sampleprepmethod_FER AFTER INSERT ON sampleprepmethod FOR EACH ROW
BEGIN
  INSERT INTO sampleprepmethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSamplePrepMethod
  , NEW.samplePrepMethod
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_sampleprepmethod_FER AFTER UPDATE ON sampleprepmethod FOR EACH ROW
BEGIN
  INSERT INTO sampleprepmethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSamplePrepMethod
  , NEW.samplePrepMethod
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_sampleprepmethod_FER AFTER DELETE ON sampleprepmethod FOR EACH ROW
BEGIN
  INSERT INTO sampleprepmethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSamplePrepMethod
  , OLD.samplePrepMethod
  , OLD.isActive );
END;
$$


--
-- Audit Table For samplesource 
--

CREATE TABLE IF NOT EXISTS `samplesource_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleSource`  int(10)  NULL DEFAULT NULL
 ,`sampleSource`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For samplesource 
--


CREATE TRIGGER TrAI_samplesource_FER AFTER INSERT ON samplesource FOR EACH ROW
BEGIN
  INSERT INTO samplesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleSource
  , NEW.sampleSource
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_samplesource_FER AFTER UPDATE ON samplesource FOR EACH ROW
BEGIN
  INSERT INTO samplesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleSource
  , NEW.sampleSource
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_samplesource_FER AFTER DELETE ON samplesource FOR EACH ROW
BEGIN
  INSERT INTO samplesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSampleSource
  , OLD.sampleSource
  , OLD.isActive );
END;
$$


--
-- Audit Table For sampletyperequestcategory 
--

CREATE TABLE IF NOT EXISTS `sampletyperequestcategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleTypeRequestCategory`  int(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sampletyperequestcategory 
--


CREATE TRIGGER TrAI_sampletyperequestcategory_FER AFTER INSERT ON sampletyperequestcategory FOR EACH ROW
BEGIN
  INSERT INTO sampletyperequestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleTypeRequestCategory
  , NEW.idSampleType
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAU_sampletyperequestcategory_FER AFTER UPDATE ON sampletyperequestcategory FOR EACH ROW
BEGIN
  INSERT INTO sampletyperequestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleTypeRequestCategory
  , NEW.idSampleType
  , NEW.codeRequestCategory );
END;
$$


CREATE TRIGGER TrAD_sampletyperequestcategory_FER AFTER DELETE ON sampletyperequestcategory FOR EACH ROW
BEGIN
  INSERT INTO sampletyperequestcategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSampleTypeRequestCategory
  , OLD.idSampleType
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For sampletype 
--

CREATE TABLE IF NOT EXISTS `sampletype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`sampleType`  varchar(50)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
 ,`notes`  varchar(5000)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sampletype 
--


CREATE TRIGGER TrAI_sampletype_FER AFTER INSERT ON sampletype FOR EACH ROW
BEGIN
  INSERT INTO sampletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleType
  , NEW.sampleType
  , NEW.sortOrder
  , NEW.isActive
  , NEW.codeNucleotideType
  , NEW.notes
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_sampletype_FER AFTER UPDATE ON sampletype FOR EACH ROW
BEGIN
  INSERT INTO sampletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSampleType
  , NEW.sampleType
  , NEW.sortOrder
  , NEW.isActive
  , NEW.codeNucleotideType
  , NEW.notes
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_sampletype_FER AFTER DELETE ON sampletype FOR EACH ROW
BEGIN
  INSERT INTO sampletype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
  , notes
  , idCoreFacility )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For sample 
--

CREATE TABLE IF NOT EXISTS `sample_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sample 
--


CREATE TRIGGER TrAI_sample_FER AFTER INSERT ON sample FOR EACH ROW
BEGIN
  INSERT INTO sample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , qcCodeApplication )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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
  , NEW.qcCodeApplication );
END;
$$


CREATE TRIGGER TrAU_sample_FER AFTER UPDATE ON sample FOR EACH ROW
BEGIN
  INSERT INTO sample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , qcCodeApplication )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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
  , NEW.qcCodeApplication );
END;
$$


CREATE TRIGGER TrAD_sample_FER AFTER DELETE ON sample FOR EACH ROW
BEGIN
  INSERT INTO sample_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , qcCodeApplication )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
  , OLD.qcCodeApplication );
END;
$$


--
-- Audit Table For scanprotocol 
--

CREATE TABLE IF NOT EXISTS `scanprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idScanProtocol`  int(10)  NULL DEFAULT NULL
 ,`scanProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For scanprotocol 
--


CREATE TRIGGER TrAI_scanprotocol_FER AFTER INSERT ON scanprotocol FOR EACH ROW
BEGIN
  INSERT INTO scanprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idScanProtocol
  , NEW.scanProtocol
  , NEW.description
  , NEW.codeRequestCategory
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_scanprotocol_FER AFTER UPDATE ON scanprotocol FOR EACH ROW
BEGIN
  INSERT INTO scanprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idScanProtocol
  , NEW.scanProtocol
  , NEW.description
  , NEW.codeRequestCategory
  , NEW.url
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_scanprotocol_FER AFTER DELETE ON scanprotocol FOR EACH ROW
BEGIN
  INSERT INTO scanprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idScanProtocol
  , scanProtocol
  , description
  , codeRequestCategory
  , url
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idScanProtocol
  , OLD.scanProtocol
  , OLD.description
  , OLD.codeRequestCategory
  , OLD.url
  , OLD.isActive );
END;
$$


--
-- Audit Table For sealtype 
--

CREATE TABLE IF NOT EXISTS `sealtype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
 ,`sealType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sealtype 
--


CREATE TRIGGER TrAI_sealtype_FER AFTER INSERT ON sealtype FOR EACH ROW
BEGIN
  INSERT INTO sealtype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSealType
  , NEW.sealType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_sealtype_FER AFTER UPDATE ON sealtype FOR EACH ROW
BEGIN
  INSERT INTO sealtype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSealType
  , NEW.sealType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_sealtype_FER AFTER DELETE ON sealtype FOR EACH ROW
BEGIN
  INSERT INTO sealtype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeSealType
  , OLD.sealType
  , OLD.isActive );
END;
$$


--
-- Audit Table For segment 
--

CREATE TABLE IF NOT EXISTS `segment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSegment`  int(10)  NULL DEFAULT NULL
 ,`length`  int(10) unsigned  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10) unsigned  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For segment 
--


CREATE TRIGGER TrAI_segment_FER AFTER INSERT ON segment FOR EACH ROW
BEGIN
  INSERT INTO segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSegment
  , NEW.length
  , NEW.name
  , NEW.idGenomeBuild
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_segment_FER AFTER UPDATE ON segment FOR EACH ROW
BEGIN
  INSERT INTO segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSegment
  , NEW.length
  , NEW.name
  , NEW.idGenomeBuild
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_segment_FER AFTER DELETE ON segment FOR EACH ROW
BEGIN
  INSERT INTO segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSegment
  , length
  , name
  , idGenomeBuild
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSegment
  , OLD.length
  , OLD.name
  , OLD.idGenomeBuild
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For seqlibprotocolapplication 
--

CREATE TABLE IF NOT EXISTS `seqlibprotocolapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For seqlibprotocolapplication 
--


CREATE TRIGGER TrAI_seqlibprotocolapplication_FER AFTER INSERT ON seqlibprotocolapplication FOR EACH ROW
BEGIN
  INSERT INTO seqlibprotocolapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqLibProtocol
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAU_seqlibprotocolapplication_FER AFTER UPDATE ON seqlibprotocolapplication FOR EACH ROW
BEGIN
  INSERT INTO seqlibprotocolapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqLibProtocol
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAD_seqlibprotocolapplication_FER AFTER DELETE ON seqlibprotocolapplication FOR EACH ROW
BEGIN
  INSERT INTO seqlibprotocolapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSeqLibProtocol
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For seqlibprotocol 
--

CREATE TABLE IF NOT EXISTS `seqlibprotocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`seqLibProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`adapterSequenceRead1`  varchar(500)  NULL DEFAULT NULL
 ,`adapterSequenceRead2`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For seqlibprotocol 
--


CREATE TRIGGER TrAI_seqlibprotocol_FER AFTER INSERT ON seqlibprotocol FOR EACH ROW
BEGIN
  INSERT INTO seqlibprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2 )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqLibProtocol
  , NEW.seqLibProtocol
  , NEW.description
  , NEW.url
  , NEW.isActive
  , NEW.adapterSequenceRead1
  , NEW.adapterSequenceRead2 );
END;
$$


CREATE TRIGGER TrAU_seqlibprotocol_FER AFTER UPDATE ON seqlibprotocol FOR EACH ROW
BEGIN
  INSERT INTO seqlibprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2 )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqLibProtocol
  , NEW.seqLibProtocol
  , NEW.description
  , NEW.url
  , NEW.isActive
  , NEW.adapterSequenceRead1
  , NEW.adapterSequenceRead2 );
END;
$$


CREATE TRIGGER TrAD_seqlibprotocol_FER AFTER DELETE ON seqlibprotocol FOR EACH ROW
BEGIN
  INSERT INTO seqlibprotocol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibProtocol
  , seqLibProtocol
  , description
  , url
  , isActive
  , adapterSequenceRead1
  , adapterSequenceRead2 )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For seqlibtreatment 
--

CREATE TABLE IF NOT EXISTS `seqlibtreatment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
 ,`seqLibTreatment`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For seqlibtreatment 
--


CREATE TRIGGER TrAI_seqlibtreatment_FER AFTER INSERT ON seqlibtreatment FOR EACH ROW
BEGIN
  INSERT INTO seqlibtreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqLibTreatment
  , NEW.seqLibTreatment
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_seqlibtreatment_FER AFTER UPDATE ON seqlibtreatment FOR EACH ROW
BEGIN
  INSERT INTO seqlibtreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqLibTreatment
  , NEW.seqLibTreatment
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_seqlibtreatment_FER AFTER DELETE ON seqlibtreatment FOR EACH ROW
BEGIN
  INSERT INTO seqlibtreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSeqLibTreatment
  , OLD.seqLibTreatment
  , OLD.isActive );
END;
$$


--
-- Audit Table For seqruntype 
--

CREATE TABLE IF NOT EXISTS `seqruntype_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`seqRunType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For seqruntype 
--


CREATE TRIGGER TrAI_seqruntype_FER AFTER INSERT ON seqruntype FOR EACH ROW
BEGIN
  INSERT INTO seqruntype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqRunType
  , NEW.seqRunType
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_seqruntype_FER AFTER UPDATE ON seqruntype FOR EACH ROW
BEGIN
  INSERT INTO seqruntype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSeqRunType
  , NEW.seqRunType
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_seqruntype_FER AFTER DELETE ON seqruntype FOR EACH ROW
BEGIN
  INSERT INTO seqruntype_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSeqRunType
  , OLD.seqRunType
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For sequencelane 
--

CREATE TABLE IF NOT EXISTS `sequencelane_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sequencelane 
--


CREATE TRIGGER TrAI_sequencelane_FER AFTER INSERT ON sequencelane FOR EACH ROW
BEGIN
  INSERT INTO sequencelane_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_sequencelane_FER AFTER UPDATE ON sequencelane FOR EACH ROW
BEGIN
  INSERT INTO sequencelane_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_sequencelane_FER AFTER DELETE ON sequencelane FOR EACH ROW
BEGIN
  INSERT INTO sequencelane_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For sequencingcontrol 
--

CREATE TABLE IF NOT EXISTS `sequencingcontrol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSequencingControl`  int(10)  NULL DEFAULT NULL
 ,`sequencingControl`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sequencingcontrol 
--


CREATE TRIGGER TrAI_sequencingcontrol_FER AFTER INSERT ON sequencingcontrol FOR EACH ROW
BEGIN
  INSERT INTO sequencingcontrol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSequencingControl
  , NEW.sequencingControl
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAU_sequencingcontrol_FER AFTER UPDATE ON sequencingcontrol FOR EACH ROW
BEGIN
  INSERT INTO sequencingcontrol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSequencingControl
  , NEW.sequencingControl
  , NEW.isActive
  , NEW.idAppUser );
END;
$$


CREATE TRIGGER TrAD_sequencingcontrol_FER AFTER DELETE ON sequencingcontrol FOR EACH ROW
BEGIN
  INSERT INTO sequencingcontrol_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSequencingControl
  , OLD.sequencingControl
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For sequencingplatform 
--

CREATE TABLE IF NOT EXISTS `sequencingplatform_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSequencingPlatform`  varchar(10)  NULL DEFAULT NULL
 ,`sequencingPlatform`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For sequencingplatform 
--


CREATE TRIGGER TrAI_sequencingplatform_FER AFTER INSERT ON sequencingplatform FOR EACH ROW
BEGIN
  INSERT INTO sequencingplatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSequencingPlatform
  , NEW.sequencingPlatform
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_sequencingplatform_FER AFTER UPDATE ON sequencingplatform FOR EACH ROW
BEGIN
  INSERT INTO sequencingplatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSequencingPlatform
  , NEW.sequencingPlatform
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_sequencingplatform_FER AFTER DELETE ON sequencingplatform FOR EACH ROW
BEGIN
  INSERT INTO sequencingplatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeSequencingPlatform
  , OLD.sequencingPlatform
  , OLD.isActive );
END;
$$


--
-- Audit Table For slidedesign 
--

CREATE TABLE IF NOT EXISTS `slidedesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`slideDesignProtocolName`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`accessionNumberArrayExpress`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For slidedesign 
--


CREATE TRIGGER TrAI_slidedesign_FER AFTER INSERT ON slidedesign FOR EACH ROW
BEGIN
  INSERT INTO slidedesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSlideDesign
  , NEW.name
  , NEW.slideDesignProtocolName
  , NEW.idSlideProduct
  , NEW.accessionNumberArrayExpress
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_slidedesign_FER AFTER UPDATE ON slidedesign FOR EACH ROW
BEGIN
  INSERT INTO slidedesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSlideDesign
  , NEW.name
  , NEW.slideDesignProtocolName
  , NEW.idSlideProduct
  , NEW.accessionNumberArrayExpress
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_slidedesign_FER AFTER DELETE ON slidedesign FOR EACH ROW
BEGIN
  INSERT INTO slidedesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlideDesign
  , name
  , slideDesignProtocolName
  , idSlideProduct
  , accessionNumberArrayExpress
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSlideDesign
  , OLD.name
  , OLD.slideDesignProtocolName
  , OLD.idSlideProduct
  , OLD.accessionNumberArrayExpress
  , OLD.isActive );
END;
$$


--
-- Audit Table For slideproductapplication 
--

CREATE TABLE IF NOT EXISTS `slideproductapplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For slideproductapplication 
--


CREATE TRIGGER TrAI_slideproductapplication_FER AFTER INSERT ON slideproductapplication FOR EACH ROW
BEGIN
  INSERT INTO slideproductapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlideProduct
  , codeApplication )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSlideProduct
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAU_slideproductapplication_FER AFTER UPDATE ON slideproductapplication FOR EACH ROW
BEGIN
  INSERT INTO slideproductapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlideProduct
  , codeApplication )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSlideProduct
  , NEW.codeApplication );
END;
$$


CREATE TRIGGER TrAD_slideproductapplication_FER AFTER DELETE ON slideproductapplication FOR EACH ROW
BEGIN
  INSERT INTO slideproductapplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlideProduct
  , codeApplication )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSlideProduct
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For slideproduct 
--

CREATE TABLE IF NOT EXISTS `slideproduct_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For slideproduct 
--


CREATE TRIGGER TrAI_slideproduct_FER AFTER INSERT ON slideproduct FOR EACH ROW
BEGIN
  INSERT INTO slideproduct_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_slideproduct_FER AFTER UPDATE ON slideproduct FOR EACH ROW
BEGIN
  INSERT INTO slideproduct_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_slideproduct_FER AFTER DELETE ON slideproduct FOR EACH ROW
BEGIN
  INSERT INTO slideproduct_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For slidesource 
--

CREATE TABLE IF NOT EXISTS `slidesource_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSlideSource`  varchar(10)  NULL DEFAULT NULL
 ,`slideSource`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For slidesource 
--


CREATE TRIGGER TrAI_slidesource_FER AFTER INSERT ON slidesource FOR EACH ROW
BEGIN
  INSERT INTO slidesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSlideSource
  , NEW.slideSource
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_slidesource_FER AFTER UPDATE ON slidesource FOR EACH ROW
BEGIN
  INSERT INTO slidesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeSlideSource
  , NEW.slideSource
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_slidesource_FER AFTER DELETE ON slidesource FOR EACH ROW
BEGIN
  INSERT INTO slidesource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeSlideSource
  , OLD.slideSource
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For slide 
--

CREATE TABLE IF NOT EXISTS `slide_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlide`  int(10)  NULL DEFAULT NULL
 ,`barcode`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`slideName`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For slide 
--


CREATE TRIGGER TrAI_slide_FER AFTER INSERT ON slide FOR EACH ROW
BEGIN
  INSERT INTO slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSlide
  , NEW.barcode
  , NEW.idSlideDesign
  , NEW.slideName );
END;
$$


CREATE TRIGGER TrAU_slide_FER AFTER UPDATE ON slide FOR EACH ROW
BEGIN
  INSERT INTO slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSlide
  , NEW.barcode
  , NEW.idSlideDesign
  , NEW.slideName );
END;
$$


CREATE TRIGGER TrAD_slide_FER AFTER DELETE ON slide FOR EACH ROW
BEGIN
  INSERT INTO slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idSlide
  , OLD.barcode
  , OLD.idSlideDesign
  , OLD.slideName );
END;
$$


--
-- Audit Table For state 
--

CREATE TABLE IF NOT EXISTS `state_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeState`  varchar(10)  NULL DEFAULT NULL
 ,`state`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For state 
--


CREATE TRIGGER TrAI_state_FER AFTER INSERT ON state FOR EACH ROW
BEGIN
  INSERT INTO state_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeState
  , state
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeState
  , NEW.state
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_state_FER AFTER UPDATE ON state FOR EACH ROW
BEGIN
  INSERT INTO state_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeState
  , state
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeState
  , NEW.state
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_state_FER AFTER DELETE ON state FOR EACH ROW
BEGIN
  INSERT INTO state_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeState
  , state
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeState
  , OLD.state
  , OLD.isActive );
END;
$$


--
-- Audit Table For step 
--

CREATE TABLE IF NOT EXISTS `step_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
 ,`step`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For step 
--


CREATE TRIGGER TrAI_step_FER AFTER INSERT ON step FOR EACH ROW
BEGIN
  INSERT INTO step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeStep
  , NEW.step
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_step_FER AFTER UPDATE ON step FOR EACH ROW
BEGIN
  INSERT INTO step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeStep
  , NEW.step
  , NEW.isActive
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_step_FER AFTER DELETE ON step FOR EACH ROW
BEGIN
  INSERT INTO step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeStep
  , OLD.step
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For submissioninstruction 
--

CREATE TABLE IF NOT EXISTS `submissioninstruction_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSubmissionInstruction`  int(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`url`  varchar(2000)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For submissioninstruction 
--


CREATE TRIGGER TrAI_submissioninstruction_FER AFTER INSERT ON submissioninstruction FOR EACH ROW
BEGIN
  INSERT INTO submissioninstruction_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idSubmissionInstruction
  , NEW.description
  , NEW.url
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.codeBioanalyzerChipType
  , NEW.idBillingSlideServiceClass );
END;
$$


CREATE TRIGGER TrAU_submissioninstruction_FER AFTER UPDATE ON submissioninstruction FOR EACH ROW
BEGIN
  INSERT INTO submissioninstruction_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idSubmissionInstruction
  , NEW.description
  , NEW.url
  , NEW.codeRequestCategory
  , NEW.codeApplication
  , NEW.codeBioanalyzerChipType
  , NEW.idBillingSlideServiceClass );
END;
$$


CREATE TRIGGER TrAD_submissioninstruction_FER AFTER DELETE ON submissioninstruction FOR EACH ROW
BEGIN
  INSERT INTO submissioninstruction_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSubmissionInstruction
  , description
  , url
  , codeRequestCategory
  , codeApplication
  , codeBioanalyzerChipType
  , idBillingSlideServiceClass )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For topic 
--

CREATE TABLE IF NOT EXISTS `topic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For topic 
--


CREATE TRIGGER TrAI_topic_FER AFTER INSERT ON topic FOR EACH ROW
BEGIN
  INSERT INTO topic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_topic_FER AFTER UPDATE ON topic FOR EACH ROW
BEGIN
  INSERT INTO topic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_topic_FER AFTER DELETE ON topic FOR EACH ROW
BEGIN
  INSERT INTO topic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
-- Audit Table For treatmententry 
--

CREATE TABLE IF NOT EXISTS `treatmententry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTreatmentEntry`  int(10)  NULL DEFAULT NULL
 ,`treatment`  varchar(2000)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For treatmententry 
--


CREATE TRIGGER TrAI_treatmententry_FER AFTER INSERT ON treatmententry FOR EACH ROW
BEGIN
  INSERT INTO treatmententry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idTreatmentEntry
  , NEW.treatment
  , NEW.idSample
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAU_treatmententry_FER AFTER UPDATE ON treatmententry FOR EACH ROW
BEGIN
  INSERT INTO treatmententry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idTreatmentEntry
  , NEW.treatment
  , NEW.idSample
  , NEW.otherLabel );
END;
$$


CREATE TRIGGER TrAD_treatmententry_FER AFTER DELETE ON treatmententry FOR EACH ROW
BEGIN
  INSERT INTO treatmententry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idTreatmentEntry
  , OLD.treatment
  , OLD.idSample
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For unloaddatatrack 
--

CREATE TABLE IF NOT EXISTS `unloaddatatrack_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idUnloadDataTrack`  int(10)  NULL DEFAULT NULL
 ,`typeName`  varchar(2000)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For unloaddatatrack 
--


CREATE TRIGGER TrAI_unloaddatatrack_FER AFTER INSERT ON unloaddatatrack FOR EACH ROW
BEGIN
  INSERT INTO unloaddatatrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idUnloadDataTrack
  , NEW.typeName
  , NEW.idAppUser
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAU_unloaddatatrack_FER AFTER UPDATE ON unloaddatatrack FOR EACH ROW
BEGIN
  INSERT INTO unloaddatatrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idUnloadDataTrack
  , NEW.typeName
  , NEW.idAppUser
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAD_unloaddatatrack_FER AFTER DELETE ON unloaddatatrack FOR EACH ROW
BEGIN
  INSERT INTO unloaddatatrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idUnloadDataTrack
  , OLD.typeName
  , OLD.idAppUser
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For userpermissionkind 
--

CREATE TABLE IF NOT EXISTS `userpermissionkind_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeUserPermissionKind`  varchar(10)  NULL DEFAULT NULL
 ,`userPermissionKind`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For userpermissionkind 
--


CREATE TRIGGER TrAI_userpermissionkind_FER AFTER INSERT ON userpermissionkind FOR EACH ROW
BEGIN
  INSERT INTO userpermissionkind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeUserPermissionKind
  , NEW.userPermissionKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_userpermissionkind_FER AFTER UPDATE ON userpermissionkind FOR EACH ROW
BEGIN
  INSERT INTO userpermissionkind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeUserPermissionKind
  , NEW.userPermissionKind
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_userpermissionkind_FER AFTER DELETE ON userpermissionkind FOR EACH ROW
BEGIN
  INSERT INTO userpermissionkind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeUserPermissionKind
  , OLD.userPermissionKind
  , OLD.isActive );
END;
$$


--
-- Audit Table For vendor 
--

CREATE TABLE IF NOT EXISTS `vendor_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`vendorName`  varchar(100)  NULL DEFAULT NULL
 ,`description`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For vendor 
--


CREATE TRIGGER TrAI_vendor_FER AFTER INSERT ON vendor FOR EACH ROW
BEGIN
  INSERT INTO vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.idVendor
  , NEW.vendorName
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_vendor_FER AFTER UPDATE ON vendor FOR EACH ROW
BEGIN
  INSERT INTO vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.idVendor
  , NEW.vendorName
  , NEW.description
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_vendor_FER AFTER DELETE ON vendor FOR EACH ROW
BEGIN
  INSERT INTO vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.idVendor
  , OLD.vendorName
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For visibility 
--

CREATE TABLE IF NOT EXISTS `visibility_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`visibility`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Triggers For visibility 
--


CREATE TRIGGER TrAI_visibility_FER AFTER INSERT ON visibility FOR EACH ROW
BEGIN
  INSERT INTO visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeVisibility
  , visibility )
  VALUES
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
  , NEW.codeVisibility
  , NEW.visibility );
END;
$$


CREATE TRIGGER TrAU_visibility_FER AFTER UPDATE ON visibility FOR EACH ROW
BEGIN
  INSERT INTO visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeVisibility
  , visibility )
  VALUES
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
  , NEW.codeVisibility
  , NEW.visibility );
END;
$$


CREATE TRIGGER TrAD_visibility_FER AFTER DELETE ON visibility FOR EACH ROW
BEGIN
  INSERT INTO visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeVisibility
  , visibility )
  VALUES
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
  , OLD.codeVisibility
  , OLD.visibility );
END;
$$


--
-- Audit Table For workitem 
--

CREATE TABLE IF NOT EXISTS `workitem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
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
) ENGINE=InnoDB
$$


--
-- Audit Triggers For workitem 
--


CREATE TRIGGER TrAI_workitem_FER AFTER INSERT ON workitem FOR EACH ROW
BEGIN
  INSERT INTO workitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'I'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAU_workitem_FER AFTER UPDATE ON workitem FOR EACH ROW
BEGIN
  INSERT INTO workitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'U'
  , CURRENT_USER()
  , NOW()
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


CREATE TRIGGER TrAD_workitem_FER AFTER DELETE ON workitem FOR EACH ROW
BEGIN
  INSERT INTO workitem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  ( USER()
  , 'D'
  , CURRENT_USER()
  , NOW()
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
