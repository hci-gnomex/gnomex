
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
DROP TRIGGER IF EXISTS TrAI_DNAPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAU_DNAPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAD_DNAPrepType_FER
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
DROP TRIGGER IF EXISTS TrAI_RNAPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAU_RNAPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAD_RNAPrepType_FER
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

CREATE TABLE IF NOT EXISTS `AlignmentPlatform_Audit` (
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
-- Initial audit table rows for AlignmentPlatform 
--

INSERT INTO AlignmentPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idAlignmentPlatform
  , alignmentPlatformName
  , webServiceName
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAlignmentPlatform
  , OLD.alignmentPlatformName
  , OLD.webServiceName
  , OLD.isActive );
END;
$$


--
-- Audit Table For AlignmentProfileGenomeIndex 
--

CREATE TABLE IF NOT EXISTS `AlignmentProfileGenomeIndex_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for AlignmentProfileGenomeIndex 
--

INSERT INTO AlignmentProfileGenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAlignmentProfile
  , idGenomeIndex )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idAlignmentProfile
  , idGenomeIndex )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAlignmentProfile
  , OLD.idGenomeIndex );
END;
$$


--
-- Audit Table For AlignmentProfile 
--

CREATE TABLE IF NOT EXISTS `AlignmentProfile_Audit` (
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
-- Initial audit table rows for AlignmentProfile 
--

INSERT INTO AlignmentProfile_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `AnalysisCollaborator_Audit` (
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
-- Initial audit table rows for AnalysisCollaborator 
--

INSERT INTO AnalysisCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idAnalysis
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAnalysis
  , OLD.idAppUser
  , OLD.canUploadData
  , OLD.canUpdate );
END;
$$


--
-- Audit Table For AnalysisExperimentItem 
--

CREATE TABLE IF NOT EXISTS `AnalysisExperimentItem_Audit` (
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
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for AnalysisExperimentItem 
--

INSERT INTO AnalysisExperimentItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest
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
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idAnalysisExperimentItem
  , NEW.idSequenceLane
  , NEW.idHybridization
  , NEW.comments
  , NEW.idAnalysis
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAU_AnalysisExperimentItem_FER AFTER UPDATE ON AnalysisExperimentItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisExperimentItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idAnalysisExperimentItem
  , NEW.idSequenceLane
  , NEW.idHybridization
  , NEW.comments
  , NEW.idAnalysis
  , NEW.idRequest );
END;
$$


CREATE TRIGGER TrAD_AnalysisExperimentItem_FER AFTER DELETE ON AnalysisExperimentItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisExperimentItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisExperimentItem
  , idSequenceLane
  , idHybridization
  , comments
  , idAnalysis
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAnalysisExperimentItem
  , OLD.idSequenceLane
  , OLD.idHybridization
  , OLD.comments
  , OLD.idAnalysis
  , OLD.idRequest );
END;
$$


--
-- Audit Table For AnalysisFile 
--

CREATE TABLE IF NOT EXISTS `AnalysisFile_Audit` (
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
-- Initial audit table rows for AnalysisFile 
--

INSERT INTO AnalysisFile_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
-- Audit Table For AnalysisGenomeBuild 
--

CREATE TABLE IF NOT EXISTS `AnalysisGenomeBuild_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for AnalysisGenomeBuild 
--

INSERT INTO AnalysisGenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idGenomeBuild )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idAnalysis
  , idGenomeBuild
  FROM AnalysisGenomeBuild
  WHERE NOT EXISTS(SELECT * FROM AnalysisGenomeBuild_Audit)
$$

--
-- Audit Triggers For AnalysisGenomeBuild 
--


CREATE TRIGGER TrAI_AnalysisGenomeBuild_FER AFTER INSERT ON AnalysisGenomeBuild FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idAnalysis
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAU_AnalysisGenomeBuild_FER AFTER UPDATE ON AnalysisGenomeBuild FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idAnalysis
  , NEW.idGenomeBuild );
END;
$$


CREATE TRIGGER TrAD_AnalysisGenomeBuild_FER AFTER DELETE ON AnalysisGenomeBuild FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGenomeBuild_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysis
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAnalysis
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For AnalysisGroupItem 
--

CREATE TABLE IF NOT EXISTS `AnalysisGroupItem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for AnalysisGroupItem 
--

INSERT INTO AnalysisGroupItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , idAnalysis )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idAnalysisGroup
  , idAnalysis
  FROM AnalysisGroupItem
  WHERE NOT EXISTS(SELECT * FROM AnalysisGroupItem_Audit)
$$

--
-- Audit Triggers For AnalysisGroupItem 
--


CREATE TRIGGER TrAI_AnalysisGroupItem_FER AFTER INSERT ON AnalysisGroupItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroupItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idAnalysisGroup
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAU_AnalysisGroupItem_FER AFTER UPDATE ON AnalysisGroupItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroupItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idAnalysisGroup
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAD_AnalysisGroupItem_FER AFTER DELETE ON AnalysisGroupItem FOR EACH ROW
BEGIN
  INSERT INTO AnalysisGroupItem_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisGroup
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAnalysisGroup
  , OLD.idAnalysis );
END;
$$


--
-- Audit Table For AnalysisGroup 
--

CREATE TABLE IF NOT EXISTS `AnalysisGroup_Audit` (
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
-- Initial audit table rows for AnalysisGroup 
--

INSERT INTO AnalysisGroup_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `AnalysisProtocol_Audit` (
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
-- Initial audit table rows for AnalysisProtocol 
--

INSERT INTO AnalysisProtocol_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `AnalysisToTopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for AnalysisToTopic 
--

INSERT INTO AnalysisToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idAnalysis )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idTopic
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idTopic
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idTopic
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idTopic
  , OLD.idAnalysis );
END;
$$


--
-- Audit Table For AnalysisType 
--

CREATE TABLE IF NOT EXISTS `AnalysisType_Audit` (
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
-- Initial audit table rows for AnalysisType 
--

INSERT INTO AnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idAnalysisType
  , analysisType
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAnalysisType
  , OLD.analysisType
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For Analysis 
--

CREATE TABLE IF NOT EXISTS `Analysis_Audit` (
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
-- Initial audit table rows for Analysis 
--

INSERT INTO Analysis_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `AnnotationReportField_Audit` (
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
-- Initial audit table rows for AnnotationReportField 
--

INSERT INTO AnnotationReportField_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `ApplicationTheme_Audit` (
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
-- Initial audit table rows for ApplicationTheme 
--

INSERT INTO ApplicationTheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idApplicationTheme
  , applicationTheme
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idApplicationTheme
  , OLD.applicationTheme
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For ApplicationType 
--

CREATE TABLE IF NOT EXISTS `ApplicationType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeApplicationType`  varchar(10)  NULL DEFAULT NULL
 ,`applicationType`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for ApplicationType 
--

INSERT INTO ApplicationType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeApplicationType
  , applicationType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeApplicationType
  , applicationType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeApplicationType
  , applicationType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeApplicationType
  , applicationType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeApplicationType
  , OLD.applicationType );
END;
$$


--
-- Audit Table For Application 
--

CREATE TABLE IF NOT EXISTS `Application_Audit` (
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
-- Initial audit table rows for Application 
--

INSERT INTO Application_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `AppUser_Audit` (
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
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for AppUser 
--

INSERT INTO AppUser_Audit
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
  , passwordExpired )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , passwordExpired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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
  , NEW.passwordExpired );
END;
$$


CREATE TRIGGER TrAU_AppUser_FER AFTER UPDATE ON AppUser FOR EACH ROW
BEGIN
  INSERT INTO AppUser_Audit
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
  , passwordExpired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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
  , NEW.passwordExpired );
END;
$$


CREATE TRIGGER TrAD_AppUser_FER AFTER DELETE ON AppUser FOR EACH ROW
BEGIN
  INSERT INTO AppUser_Audit
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
  , passwordExpired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
  , OLD.passwordExpired );
END;
$$


--
-- Audit Table For ArrayCoordinate 
--

CREATE TABLE IF NOT EXISTS `ArrayCoordinate_Audit` (
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
-- Initial audit table rows for ArrayCoordinate 
--

INSERT INTO ArrayCoordinate_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `ArrayDesign_Audit` (
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
-- Initial audit table rows for ArrayDesign 
--

INSERT INTO ArrayDesign_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idArrayDesign
  , name
  , accessionNumberUArrayExpress
  , idArrayCoordinate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idArrayDesign
  , OLD.name
  , OLD.accessionNumberUArrayExpress
  , OLD.idArrayCoordinate );
END;
$$


--
-- Audit Table For Assay 
--

CREATE TABLE IF NOT EXISTS `Assay_Audit` (
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
-- Initial audit table rows for Assay 
--

INSERT INTO Assay_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idAssay
  , name
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idAssay
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idAssay
  , OLD.name
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingAccountUser 
--

CREATE TABLE IF NOT EXISTS `BillingAccountUser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for BillingAccountUser 
--

INSERT INTO BillingAccountUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingAccount
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idBillingAccount
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idBillingAccount
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idBillingAccount
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idBillingAccount
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For BillingAccount 
--

CREATE TABLE IF NOT EXISTS `BillingAccount_Audit` (
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
 ,`ZipCode`  varchar(20)  NULL DEFAULT NULL
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
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for BillingAccount 
--

INSERT INTO BillingAccount_Audit
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
  , ZipCode
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
  , custom3 )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , ZipCode
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
  , ZipCode
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
  , custom3 )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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
  , NEW.ZipCode
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
  , NEW.custom3 );
END;
$$


CREATE TRIGGER TrAU_BillingAccount_FER AFTER UPDATE ON BillingAccount FOR EACH ROW
BEGIN
  INSERT INTO BillingAccount_Audit
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
  , ZipCode
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
  , custom3 )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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
  , NEW.ZipCode
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
  , NEW.custom3 );
END;
$$


CREATE TRIGGER TrAD_BillingAccount_FER AFTER DELETE ON BillingAccount FOR EACH ROW
BEGIN
  INSERT INTO BillingAccount_Audit
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
  , ZipCode
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
  , custom3 )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
  , OLD.ZipCode
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
  , OLD.custom3 );
END;
$$


--
-- Audit Table For BillingChargeKind 
--

CREATE TABLE IF NOT EXISTS `BillingChargeKind_Audit` (
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
-- Initial audit table rows for BillingChargeKind 
--

INSERT INTO BillingChargeKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeBillingChargeKind
  , billingChargeKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeBillingChargeKind
  , OLD.billingChargeKind
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingItem 
--

CREATE TABLE IF NOT EXISTS `BillingItem_Audit` (
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
 ,`percentagePrice`  decimal(3,2)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`completeDate`  datetime  NULL DEFAULT NULL
 ,`splitType`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idDiskUsageByMonth`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idProductLineItem`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for BillingItem 
--

INSERT INTO BillingItem_Audit
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
  , idProductOrder
  , idProductLineItem )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idProductLineItem
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
  , idProductLineItem )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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
  , NEW.idProductOrder
  , NEW.idProductLineItem );
END;
$$


CREATE TRIGGER TrAU_BillingItem_FER AFTER UPDATE ON BillingItem FOR EACH ROW
BEGIN
  INSERT INTO BillingItem_Audit
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
  , idProductOrder
  , idProductLineItem )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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
  , NEW.idProductOrder
  , NEW.idProductLineItem );
END;
$$


CREATE TRIGGER TrAD_BillingItem_FER AFTER DELETE ON BillingItem FOR EACH ROW
BEGIN
  INSERT INTO BillingItem_Audit
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
  , idProductOrder
  , idProductLineItem )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
  , OLD.idProductOrder
  , OLD.idProductLineItem );
END;
$$


--
-- Audit Table For BillingPeriod 
--

CREATE TABLE IF NOT EXISTS `BillingPeriod_Audit` (
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
-- Initial audit table rows for BillingPeriod 
--

INSERT INTO BillingPeriod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , idBillingPeriod
  , billingPeriod
  , startDate
  , endDate
  , isActive
  FROM BillingPeriod
  WHERE NOT EXISTS(SELECT * FROM BillingPeriod_Audit)
$$

--
-- Audit Triggers For BillingPeriod 
--


CREATE TRIGGER TrAI_BillingPeriod_FER AFTER INSERT ON BillingPeriod FOR EACH ROW
BEGIN
  INSERT INTO BillingPeriod_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idBillingPeriod
  , NEW.billingPeriod
  , NEW.startDate
  , NEW.endDate
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_BillingPeriod_FER AFTER UPDATE ON BillingPeriod FOR EACH ROW
BEGIN
  INSERT INTO BillingPeriod_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idBillingPeriod
  , NEW.billingPeriod
  , NEW.startDate
  , NEW.endDate
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_BillingPeriod_FER AFTER DELETE ON BillingPeriod FOR EACH ROW
BEGIN
  INSERT INTO BillingPeriod_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `BillingSlideProductClass_Audit` (
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
-- Initial audit table rows for BillingSlideProductClass 
--

INSERT INTO BillingSlideProductClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idBillingSlideProductClass
  , billingSlideProductClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idBillingSlideProductClass
  , OLD.billingSlideProductClass
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingSlideServiceClass 
--

CREATE TABLE IF NOT EXISTS `BillingSlideServiceClass_Audit` (
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
-- Initial audit table rows for BillingSlideServiceClass 
--

INSERT INTO BillingSlideServiceClass_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idBillingSlideServiceClass
  , billingSlideServiceClass
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idBillingSlideServiceClass
  , OLD.billingSlideServiceClass
  , OLD.isActive );
END;
$$


--
-- Audit Table For BillingStatus 
--

CREATE TABLE IF NOT EXISTS `BillingStatus_Audit` (
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
-- Initial audit table rows for BillingStatus 
--

INSERT INTO BillingStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeBillingStatus
  , billingStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeBillingStatus
  , billingStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeBillingStatus
  , OLD.billingStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For BioanalyzerChipType 
--

CREATE TABLE IF NOT EXISTS `BioanalyzerChipType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`bioanalyzerChipType`  varchar(50)  NULL DEFAULT NULL
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
-- Initial audit table rows for BioanalyzerChipType 
--

INSERT INTO BioanalyzerChipType_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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


CREATE TRIGGER TrAU_BioanalyzerChipType_FER AFTER UPDATE ON BioanalyzerChipType FOR EACH ROW
BEGIN
  INSERT INTO BioanalyzerChipType_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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


CREATE TRIGGER TrAD_BioanalyzerChipType_FER AFTER DELETE ON BioanalyzerChipType FOR EACH ROW
BEGIN
  INSERT INTO BioanalyzerChipType_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
-- Audit Table For Chromatogram 
--

CREATE TABLE IF NOT EXISTS `Chromatogram_Audit` (
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
-- Initial audit table rows for Chromatogram 
--

INSERT INTO Chromatogram_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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


CREATE TRIGGER TrAU_Chromatogram_FER AFTER UPDATE ON Chromatogram FOR EACH ROW
BEGIN
  INSERT INTO Chromatogram_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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


CREATE TRIGGER TrAD_Chromatogram_FER AFTER DELETE ON Chromatogram FOR EACH ROW
BEGIN
  INSERT INTO Chromatogram_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
-- Audit Table For ConcentrationUnit 
--

CREATE TABLE IF NOT EXISTS `ConcentrationUnit_Audit` (
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
-- Initial audit table rows for ConcentrationUnit 
--

INSERT INTO ConcentrationUnit_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `ContextSensitiveHelp_Audit` (
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
-- Initial audit table rows for ContextSensitiveHelp 
--

INSERT INTO ContextSensitiveHelp_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `CoreFacilityLab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for CoreFacilityLab 
--

INSERT INTO CoreFacilityLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idLab )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idCoreFacility
  , OLD.idLab );
END;
$$


--
-- Audit Table For CoreFacilityManager 
--

CREATE TABLE IF NOT EXISTS `CoreFacilityManager_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for CoreFacilityManager 
--

INSERT INTO CoreFacilityManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idCoreFacility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For CoreFacilitySubmitter 
--

CREATE TABLE IF NOT EXISTS `CoreFacilitySubmitter_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for CoreFacilitySubmitter 
--

INSERT INTO CoreFacilitySubmitter_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCoreFacility
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idCoreFacility
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idCoreFacility
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For CoreFacility 
--

CREATE TABLE IF NOT EXISTS `CoreFacility_Audit` (
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
-- Initial audit table rows for CoreFacility 
--

INSERT INTO CoreFacility_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `CreditCardCompany_Audit` (
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
-- Initial audit table rows for CreditCardCompany 
--

INSERT INTO CreditCardCompany_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idCreditCardCompany
  , name
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idCreditCardCompany
  , OLD.name
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For DataTrackCollaborator 
--

CREATE TABLE IF NOT EXISTS `DataTrackCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for DataTrackCollaborator 
--

INSERT INTO DataTrackCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idDataTrack
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idDataTrack
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idDataTrack
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idDataTrack
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For DataTrackFile 
--

CREATE TABLE IF NOT EXISTS `DataTrackFile_Audit` (
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
-- Initial audit table rows for DataTrackFile 
--

INSERT INTO DataTrackFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idDataTrackFile
  , idAnalysisFile
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idDataTrackFile
  , OLD.idAnalysisFile
  , OLD.idDataTrack );
END;
$$


--
-- Audit Table For DataTrackFolder 
--

CREATE TABLE IF NOT EXISTS `DataTrackFolder_Audit` (
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
-- Initial audit table rows for DataTrackFolder 
--

INSERT INTO DataTrackFolder_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `DataTrackToFolder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for DataTrackToFolder 
--

INSERT INTO DataTrackToFolder_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idDataTrack
  , idDataTrackFolder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idDataTrack
  , idDataTrackFolder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idDataTrack
  , OLD.idDataTrackFolder );
END;
$$


--
-- Audit Table For DataTrackToTopic 
--

CREATE TABLE IF NOT EXISTS `DataTrackToTopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for DataTrackToTopic 
--

INSERT INTO DataTrackToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idDataTrack )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idTopic
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idTopic
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idTopic
  , idDataTrack )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idTopic
  , OLD.idDataTrack );
END;
$$


--
-- Audit Table For DataTrack 
--

CREATE TABLE IF NOT EXISTS `DataTrack_Audit` (
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
-- Initial audit table rows for DataTrack 
--

INSERT INTO DataTrack_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `DiskUsageByMonth_Audit` (
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
-- Initial audit table rows for DiskUsageByMonth 
--

INSERT INTO DiskUsageByMonth_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
-- Audit Table For DNAPrepType 
--

CREATE TABLE IF NOT EXISTS `DNAPrepType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeDNAPrepType`  varchar(10)  NULL DEFAULT NULL
 ,`dnaPrepType`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for DNAPrepType 
--

INSERT INTO DNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeDNAPrepType
  , dnaPrepType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , codeDNAPrepType
  , dnaPrepType
  , isActive
  FROM DNAPrepType
  WHERE NOT EXISTS(SELECT * FROM DNAPrepType_Audit)
$$

--
-- Audit Triggers For DNAPrepType 
--


CREATE TRIGGER TrAI_DNAPrepType_FER AFTER INSERT ON DNAPrepType FOR EACH ROW
BEGIN
  INSERT INTO DNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeDNAPrepType
  , dnaPrepType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.codeDNAPrepType
  , NEW.dnaPrepType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_DNAPrepType_FER AFTER UPDATE ON DNAPrepType FOR EACH ROW
BEGIN
  INSERT INTO DNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeDNAPrepType
  , dnaPrepType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.codeDNAPrepType
  , NEW.dnaPrepType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_DNAPrepType_FER AFTER DELETE ON DNAPrepType FOR EACH ROW
BEGIN
  INSERT INTO DNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeDNAPrepType
  , dnaPrepType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeDNAPrepType
  , OLD.dnaPrepType
  , OLD.isActive );
END;
$$


--
-- Audit Table For ExperimentDesignEntry 
--

CREATE TABLE IF NOT EXISTS `ExperimentDesignEntry_Audit` (
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
-- Initial audit table rows for ExperimentDesignEntry 
--

INSERT INTO ExperimentDesignEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `ExperimentDesign_Audit` (
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
-- Initial audit table rows for ExperimentDesign 
--

INSERT INTO ExperimentDesign_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `ExperimentFactorEntry_Audit` (
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
-- Initial audit table rows for ExperimentFactorEntry 
--

INSERT INTO ExperimentFactorEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `ExperimentFactor_Audit` (
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
-- Initial audit table rows for ExperimentFactor 
--

INSERT INTO ExperimentFactor_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `ExperimentFile_Audit` (
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
-- Initial audit table rows for ExperimentFile 
--

INSERT INTO ExperimentFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , OLD.idExperimentFile
  , OLD.fileName
  , OLD.fileSize
  , OLD.idRequest
  , OLD.createDate );
END;
$$


--
-- Audit Table For FAQ 
--

CREATE TABLE IF NOT EXISTS `FAQ_Audit` (
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
-- Initial audit table rows for FAQ 
--

INSERT INTO FAQ_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFAQ
  , title
  , url
  , idCoreFacility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idFAQ
  , title
  , url
  , idCoreFacility
  FROM FAQ
  WHERE NOT EXISTS(SELECT * FROM FAQ_Audit)
$$

--
-- Audit Triggers For FAQ 
--


CREATE TRIGGER TrAI_FAQ_FER AFTER INSERT ON FAQ FOR EACH ROW
BEGIN
  INSERT INTO FAQ_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idFAQ
  , NEW.title
  , NEW.url
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAU_FAQ_FER AFTER UPDATE ON FAQ FOR EACH ROW
BEGIN
  INSERT INTO FAQ_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idFAQ
  , NEW.title
  , NEW.url
  , NEW.idCoreFacility );
END;
$$


CREATE TRIGGER TrAD_FAQ_FER AFTER DELETE ON FAQ FOR EACH ROW
BEGIN
  INSERT INTO FAQ_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFAQ
  , title
  , url
  , idCoreFacility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idFAQ
  , OLD.title
  , OLD.url
  , OLD.idCoreFacility );
END;
$$


--
-- Audit Table For FeatureExtractionProtocol 
--

CREATE TABLE IF NOT EXISTS `FeatureExtractionProtocol_Audit` (
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
-- Initial audit table rows for FeatureExtractionProtocol 
--

INSERT INTO FeatureExtractionProtocol_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `FlowCellChannel_Audit` (
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
-- Initial audit table rows for FlowCellChannel 
--

INSERT INTO FlowCellChannel_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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


CREATE TRIGGER TrAU_FlowCellChannel_FER AFTER UPDATE ON FlowCellChannel FOR EACH ROW
BEGIN
  INSERT INTO FlowCellChannel_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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


CREATE TRIGGER TrAD_FlowCellChannel_FER AFTER DELETE ON FlowCellChannel FOR EACH ROW
BEGIN
  INSERT INTO FlowCellChannel_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
-- Audit Table For FlowCell 
--

CREATE TABLE IF NOT EXISTS `FlowCell_Audit` (
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
-- Initial audit table rows for FlowCell 
--

INSERT INTO FlowCell_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `FundingAgency_Audit` (
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
-- Initial audit table rows for FundingAgency 
--

INSERT INTO FundingAgency_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idFundingAgency
  , fundingAgency
  , isPeerReviewedFunding
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idFundingAgency
  , OLD.fundingAgency
  , OLD.isPeerReviewedFunding
  , OLD.isActive );
END;
$$


--
-- Audit Table For GenomeBuildAlias 
--

CREATE TABLE IF NOT EXISTS `GenomeBuildAlias_Audit` (
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
-- Initial audit table rows for GenomeBuildAlias 
--

INSERT INTO GenomeBuildAlias_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idGenomeBuildAlias
  , alias
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idGenomeBuildAlias
  , OLD.alias
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For GenomeBuild 
--

CREATE TABLE IF NOT EXISTS `GenomeBuild_Audit` (
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
-- Initial audit table rows for GenomeBuild 
--

INSERT INTO GenomeBuild_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `GenomeIndex_Audit` (
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
-- Initial audit table rows for GenomeIndex 
--

INSERT INTO GenomeIndex_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `HybProtocol_Audit` (
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
-- Initial audit table rows for HybProtocol 
--

INSERT INTO HybProtocol_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Hybridization_Audit` (
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
-- Initial audit table rows for Hybridization 
--

INSERT INTO Hybridization_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `InstitutionLab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for InstitutionLab 
--

INSERT INTO InstitutionLab_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstitution
  , idLab )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idInstitution
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idInstitution
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idInstitution
  , idLab )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idInstitution
  , OLD.idLab );
END;
$$


--
-- Audit Table For Institution 
--

CREATE TABLE IF NOT EXISTS `Institution_Audit` (
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
-- Initial audit table rows for Institution 
--

INSERT INTO Institution_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `InstrumentRunStatus_Audit` (
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
-- Initial audit table rows for InstrumentRunStatus 
--

INSERT INTO InstrumentRunStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeInstrumentRunStatus
  , instrumentRunStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeInstrumentRunStatus
  , OLD.instrumentRunStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For InstrumentRun 
--

CREATE TABLE IF NOT EXISTS `InstrumentRun_Audit` (
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
-- Initial audit table rows for InstrumentRun 
--

INSERT INTO InstrumentRun_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Instrument_Audit` (
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
-- Initial audit table rows for Instrument 
--

INSERT INTO Instrument_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idInstrument
  , instrument
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idInstrument
  , instrument
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idInstrument
  , OLD.instrument
  , OLD.isActive );
END;
$$


--
-- Audit Table For InternalAccountFieldsConfiguration 
--

CREATE TABLE IF NOT EXISTS `InternalAccountFieldsConfiguration_Audit` (
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
-- Initial audit table rows for InternalAccountFieldsConfiguration 
--

INSERT INTO InternalAccountFieldsConfiguration_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Invoice_Audit` (
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
-- Initial audit table rows for Invoice 
--

INSERT INTO Invoice_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , OLD.idInvoice
  , OLD.idCoreFacility
  , OLD.idBillingPeriod
  , OLD.idBillingAccount
  , OLD.invoiceNumber
  , OLD.lastEmailDate );
END;
$$


--
-- Audit Table For IScanChip 
--

CREATE TABLE IF NOT EXISTS `IScanChip_Audit` (
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
-- Initial audit table rows for IScanChip 
--

INSERT INTO IScanChip_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idIScanChip
  , name
  , costPerSample
  , samplesPerChip
  , chipsPerKit
  , markersPerSample
  , catalogNumber
  , isActive
  FROM IScanChip
  WHERE NOT EXISTS(SELECT * FROM IScanChip_Audit)
$$

--
-- Audit Triggers For IScanChip 
--


CREATE TRIGGER TrAI_IScanChip_FER AFTER INSERT ON IScanChip FOR EACH ROW
BEGIN
  INSERT INTO IScanChip_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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


CREATE TRIGGER TrAU_IScanChip_FER AFTER UPDATE ON IScanChip FOR EACH ROW
BEGIN
  INSERT INTO IScanChip_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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


CREATE TRIGGER TrAD_IScanChip_FER AFTER DELETE ON IScanChip FOR EACH ROW
BEGIN
  INSERT INTO IScanChip_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
-- Audit Table For LabCollaborator 
--

CREATE TABLE IF NOT EXISTS `LabCollaborator_Audit` (
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
-- Initial audit table rows for LabCollaborator 
--

INSERT INTO LabCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For LabeledSample 
--

CREATE TABLE IF NOT EXISTS `LabeledSample_Audit` (
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
-- Initial audit table rows for LabeledSample 
--

INSERT INTO LabeledSample_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `LabelingProtocol_Audit` (
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
-- Initial audit table rows for LabelingProtocol 
--

INSERT INTO LabelingProtocol_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `LabelingReactionSize_Audit` (
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
-- Initial audit table rows for LabelingReactionSize 
--

INSERT INTO LabelingReactionSize_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeLabelingReactionSize
  , labelingReactionSize
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeLabelingReactionSize
  , OLD.labelingReactionSize
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For Label 
--

CREATE TABLE IF NOT EXISTS `Label_Audit` (
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
-- Initial audit table rows for Label 
--

INSERT INTO Label_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLabel
  , label
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idLabel
  , label
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idLabel
  , label
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idLabel
  , label
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idLabel
  , OLD.label
  , OLD.isActive );
END;
$$


--
-- Audit Table For LabManager 
--

CREATE TABLE IF NOT EXISTS `LabManager_Audit` (
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
-- Initial audit table rows for LabManager 
--

INSERT INTO LabManager_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idLab
  , idAppUser
  , sendUploadAlert )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idLab
  , idAppUser
  , sendUploadAlert )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idLab
  , OLD.idAppUser
  , OLD.sendUploadAlert );
END;
$$


--
-- Audit Table For LabUser 
--

CREATE TABLE IF NOT EXISTS `LabUser_Audit` (
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
-- Initial audit table rows for LabUser 
--

INSERT INTO LabUser_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `Lab_Audit` (
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
-- Initial audit table rows for Lab 
--

INSERT INTO Lab_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
-- Audit Table For MetrixObject 
--

CREATE TABLE IF NOT EXISTS `MetrixObject_Audit` (
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
-- Initial audit table rows for MetrixObject 
--

INSERT INTO MetrixObject_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , id
  , run_id
  , object_value
  , state )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , id
  , run_id
  , object_value
  , state )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.id
  , OLD.run_id
  , OLD.object_value
  , OLD.state );
END;
$$


--
-- Audit Table For NewsItem 
--

CREATE TABLE IF NOT EXISTS `NewsItem_Audit` (
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
-- Initial audit table rows for NewsItem 
--

INSERT INTO NewsItem_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Notification_Audit` (
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
-- Initial audit table rows for Notification 
--

INSERT INTO Notification_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `NucleotideType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for NucleotideType 
--

INSERT INTO NucleotideType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeNucleotideType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeNucleotideType );
END;
$$


--
-- Audit Table For NumberSequencingCyclesAllowed 
--

CREATE TABLE IF NOT EXISTS `NumberSequencingCyclesAllowed_Audit` (
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
-- Initial audit table rows for NumberSequencingCyclesAllowed 
--

INSERT INTO NumberSequencingCyclesAllowed_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `NumberSequencingCycles_Audit` (
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
-- Initial audit table rows for NumberSequencingCycles 
--

INSERT INTO NumberSequencingCycles_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `OligoBarcodeSchemeAllowed_Audit` (
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
-- Initial audit table rows for OligoBarcodeSchemeAllowed 
--

INSERT INTO OligoBarcodeSchemeAllowed_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idOligoBarcodeSchemeAllowed
  , idOligoBarcodeScheme
  , idSeqLibProtocol
  , isIndexGroupB )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idOligoBarcodeSchemeAllowed
  , OLD.idOligoBarcodeScheme
  , OLD.idSeqLibProtocol
  , OLD.isIndexGroupB );
END;
$$


--
-- Audit Table For OligoBarcodeScheme 
--

CREATE TABLE IF NOT EXISTS `OligoBarcodeScheme_Audit` (
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
-- Initial audit table rows for OligoBarcodeScheme 
--

INSERT INTO OligoBarcodeScheme_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idOligoBarcodeScheme
  , oligoBarcodeScheme
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idOligoBarcodeScheme
  , OLD.oligoBarcodeScheme
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For OligoBarcode 
--

CREATE TABLE IF NOT EXISTS `OligoBarcode_Audit` (
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
-- Initial audit table rows for OligoBarcode 
--

INSERT INTO OligoBarcode_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Organism_Audit` (
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
-- Initial audit table rows for Organism 
--

INSERT INTO Organism_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `OtherAccountFieldsConfiguration_Audit` (
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
-- Initial audit table rows for OtherAccountFieldsConfiguration 
--

INSERT INTO OtherAccountFieldsConfiguration_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idOtherAccountFieldsConfiguration
  , fieldName
  , include
  , isRequired )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idOtherAccountFieldsConfiguration
  , OLD.fieldName
  , OLD.include
  , OLD.isRequired );
END;
$$


--
-- Audit Table For PlateType 
--

CREATE TABLE IF NOT EXISTS `PlateType_Audit` (
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
-- Initial audit table rows for PlateType 
--

INSERT INTO PlateType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePlateType
  , plateTypeDescription
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codePlateType
  , plateTypeDescription
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codePlateType
  , OLD.plateTypeDescription
  , OLD.isActive );
END;
$$


--
-- Audit Table For PlateWell 
--

CREATE TABLE IF NOT EXISTS `PlateWell_Audit` (
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
-- Initial audit table rows for PlateWell 
--

INSERT INTO PlateWell_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Plate_Audit` (
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
-- Initial audit table rows for Plate 
--

INSERT INTO Plate_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `PriceCategoryStep_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for PriceCategoryStep 
--

INSERT INTO PriceCategoryStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCategory
  , codeStep )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPriceCategory
  , codeStep )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPriceCategory
  , codeStep )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPriceCategory
  , codeStep )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPriceCategory
  , OLD.codeStep );
END;
$$


--
-- Audit Table For PriceCategory 
--

CREATE TABLE IF NOT EXISTS `PriceCategory_Audit` (
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
-- Initial audit table rows for PriceCategory 
--

INSERT INTO PriceCategory_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `PriceCriteria_Audit` (
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
-- Initial audit table rows for PriceCriteria 
--

INSERT INTO PriceCriteria_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPriceCriteria
  , filter1
  , filter2
  , idPrice )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPriceCriteria
  , OLD.filter1
  , OLD.filter2
  , OLD.idPrice );
END;
$$


--
-- Audit Table For PriceSheetPriceCategory 
--

CREATE TABLE IF NOT EXISTS `PriceSheetPriceCategory_Audit` (
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
-- Initial audit table rows for PriceSheetPriceCategory 
--

INSERT INTO PriceSheetPriceCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPriceSheet
  , idPriceCategory
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPriceSheet
  , OLD.idPriceCategory
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For PriceSheetRequestCategory 
--

CREATE TABLE IF NOT EXISTS `PriceSheetRequestCategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for PriceSheetRequestCategory 
--

INSERT INTO PriceSheetRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , codeRequestCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPriceSheet
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPriceSheet
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For PriceSheet 
--

CREATE TABLE IF NOT EXISTS `PriceSheet_Audit` (
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
-- Initial audit table rows for PriceSheet 
--

INSERT INTO PriceSheet_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPriceSheet
  , name
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPriceSheet
  , name
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPriceSheet
  , OLD.name
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For Price 
--

CREATE TABLE IF NOT EXISTS `Price_Audit` (
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
-- Initial audit table rows for Price 
--

INSERT INTO Price_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Primer_Audit` (
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
-- Initial audit table rows for Primer 
--

INSERT INTO Primer_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `ProductLedger_Audit` (
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
 ,`requestNumber`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for ProductLedger 
--

INSERT INTO ProductLedger_Audit
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
  , requestNumber )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , requestNumber
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
  , idProductLedger
  , idLab
  , idProduct
  , qty
  , comment
  , timeStame
  , idProductOrder
  , requestNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idProductLedger
  , NEW.idLab
  , NEW.idProduct
  , NEW.qty
  , NEW.comment
  , NEW.timeStame
  , NEW.idProductOrder
  , NEW.requestNumber );
END;
$$


CREATE TRIGGER TrAU_ProductLedger_FER AFTER UPDATE ON ProductLedger FOR EACH ROW
BEGIN
  INSERT INTO ProductLedger_Audit
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
  , requestNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idProductLedger
  , NEW.idLab
  , NEW.idProduct
  , NEW.qty
  , NEW.comment
  , NEW.timeStame
  , NEW.idProductOrder
  , NEW.requestNumber );
END;
$$


CREATE TRIGGER TrAD_ProductLedger_FER AFTER DELETE ON ProductLedger FOR EACH ROW
BEGIN
  INSERT INTO ProductLedger_Audit
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
  , requestNumber )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idProductLedger
  , OLD.idLab
  , OLD.idProduct
  , OLD.qty
  , OLD.comment
  , OLD.timeStame
  , OLD.idProductOrder
  , OLD.requestNumber );
END;
$$


--
-- Audit Table For ProductLineItem 
--

CREATE TABLE IF NOT EXISTS `ProductLineItem_Audit` (
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
-- Initial audit table rows for ProductLineItem 
--

INSERT INTO ProductLineItem_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `ProductOrderFile_Audit` (
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
-- Initial audit table rows for ProductOrderFile 
--

INSERT INTO ProductOrderFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate
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
  , idProductOrderFile
  , idProductOrder
  , fileName
  , fileSize
  , createDate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idProductOrderFile
  , NEW.idProductOrder
  , NEW.fileName
  , NEW.fileSize
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAU_ProductOrderFile_FER AFTER UPDATE ON ProductOrderFile FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderFile_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idProductOrderFile
  , NEW.idProductOrder
  , NEW.fileName
  , NEW.fileSize
  , NEW.createDate );
END;
$$


CREATE TRIGGER TrAD_ProductOrderFile_FER AFTER DELETE ON ProductOrderFile FOR EACH ROW
BEGIN
  INSERT INTO ProductOrderFile_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idProductOrderFile
  , OLD.idProductOrder
  , OLD.fileName
  , OLD.fileSize
  , OLD.createDate );
END;
$$


--
-- Audit Table For ProductOrderStatus 
--

CREATE TABLE IF NOT EXISTS `ProductOrderStatus_Audit` (
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
-- Initial audit table rows for ProductOrderStatus 
--

INSERT INTO ProductOrderStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeProductOrderStatus
  , productOrderStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeProductOrderStatus
  , OLD.productOrderStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For ProductOrder 
--

CREATE TABLE IF NOT EXISTS `ProductOrder_Audit` (
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
 ,`idBillingAccount`  int(11)  NULL DEFAULT NULL
 ,`productOrderNumber`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for ProductOrder 
--

INSERT INTO ProductOrder_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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


CREATE TRIGGER TrAU_ProductOrder_FER AFTER UPDATE ON ProductOrder FOR EACH ROW
BEGIN
  INSERT INTO ProductOrder_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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


CREATE TRIGGER TrAD_ProductOrder_FER AFTER DELETE ON ProductOrder FOR EACH ROW
BEGIN
  INSERT INTO ProductOrder_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
-- Audit Table For ProductType 
--

CREATE TABLE IF NOT EXISTS `ProductType_Audit` (
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
-- Initial audit table rows for ProductType 
--

INSERT INTO ProductType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , codeProductType
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
  , codeProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.codeProductType
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
  , codeProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.codeProductType
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
  , codeProductType
  , description
  , idCoreFacility
  , idVendor
  , idPriceCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeProductType
  , OLD.description
  , OLD.idCoreFacility
  , OLD.idVendor
  , OLD.idPriceCategory );
END;
$$


--
-- Audit Table For Product 
--

CREATE TABLE IF NOT EXISTS `Product_Audit` (
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
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for Product 
--

INSERT INTO Product_Audit
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
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idProduct
  , name
  , codeProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive
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
  , idProduct
  , name
  , codeProductType
  , idPrice
  , orderQty
  , useQty
  , catalogNumber
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idProduct
  , NEW.name
  , NEW.codeProductType
  , NEW.idPrice
  , NEW.orderQty
  , NEW.useQty
  , NEW.catalogNumber
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_Product_FER AFTER UPDATE ON Product FOR EACH ROW
BEGIN
  INSERT INTO Product_Audit
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
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idProduct
  , NEW.name
  , NEW.codeProductType
  , NEW.idPrice
  , NEW.orderQty
  , NEW.useQty
  , NEW.catalogNumber
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_Product_FER AFTER DELETE ON Product FOR EACH ROW
BEGIN
  INSERT INTO Product_Audit
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
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idProduct
  , OLD.name
  , OLD.codeProductType
  , OLD.idPrice
  , OLD.orderQty
  , OLD.useQty
  , OLD.catalogNumber
  , OLD.isActive );
END;
$$


--
-- Audit Table For Project 
--

CREATE TABLE IF NOT EXISTS `Project_Audit` (
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
-- Initial audit table rows for Project 
--

INSERT INTO Project_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `PropertyAnalysisType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for PropertyAnalysisType 
--

INSERT INTO PropertyAnalysisType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idAnalysisType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idProperty
  , idAnalysisType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idProperty
  , idAnalysisType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idProperty
  , idAnalysisType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idProperty
  , OLD.idAnalysisType );
END;
$$


--
-- Audit Table For PropertyDictionary 
--

CREATE TABLE IF NOT EXISTS `PropertyDictionary_Audit` (
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
-- Initial audit table rows for PropertyDictionary 
--

INSERT INTO PropertyDictionary_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `PropertyEntryOption_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for PropertyEntryOption 
--

INSERT INTO PropertyEntryOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntry
  , idPropertyOption )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPropertyEntry
  , idPropertyOption )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPropertyEntry
  , OLD.idPropertyOption );
END;
$$


--
-- Audit Table For PropertyEntryValue 
--

CREATE TABLE IF NOT EXISTS `PropertyEntryValue_Audit` (
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
-- Initial audit table rows for PropertyEntryValue 
--

INSERT INTO PropertyEntryValue_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPropertyEntryValue
  , value
  , idPropertyEntry )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPropertyEntryValue
  , OLD.value
  , OLD.idPropertyEntry );
END;
$$


--
-- Audit Table For PropertyEntry 
--

CREATE TABLE IF NOT EXISTS `PropertyEntry_Audit` (
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
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for PropertyEntry 
--

INSERT INTO PropertyEntry_Audit
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
  , idAnalysis )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis
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
  , idPropertyEntry
  , idProperty
  , idSample
  , valueString
  , otherLabel
  , idDataTrack
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idPropertyEntry
  , NEW.idProperty
  , NEW.idSample
  , NEW.valueString
  , NEW.otherLabel
  , NEW.idDataTrack
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAU_PropertyEntry_FER AFTER UPDATE ON PropertyEntry FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntry_Audit
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
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idPropertyEntry
  , NEW.idProperty
  , NEW.idSample
  , NEW.valueString
  , NEW.otherLabel
  , NEW.idDataTrack
  , NEW.idAnalysis );
END;
$$


CREATE TRIGGER TrAD_PropertyEntry_FER AFTER DELETE ON PropertyEntry FOR EACH ROW
BEGIN
  INSERT INTO PropertyEntry_Audit
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
  , idAnalysis )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPropertyEntry
  , OLD.idProperty
  , OLD.idSample
  , OLD.valueString
  , OLD.otherLabel
  , OLD.idDataTrack
  , OLD.idAnalysis );
END;
$$


--
-- Audit Table For PropertyOption 
--

CREATE TABLE IF NOT EXISTS `PropertyOption_Audit` (
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
-- Initial audit table rows for PropertyOption 
--

INSERT INTO PropertyOption_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `PropertyOrganism_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for PropertyOrganism 
--

INSERT INTO PropertyOrganism_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idProperty
  , idOrganism )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idProperty
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idProperty
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idProperty
  , idOrganism )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idProperty
  , OLD.idOrganism );
END;
$$


--
-- Audit Table For PropertyPlatformApplication 
--

CREATE TABLE IF NOT EXISTS `PropertyPlatformApplication_Audit` (
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
-- Initial audit table rows for PropertyPlatformApplication 
--

INSERT INTO PropertyPlatformApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idPlatformApplication
  , idProperty
  , codeRequestCategory
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idPlatformApplication
  , OLD.idProperty
  , OLD.codeRequestCategory
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For PropertyType 
--

CREATE TABLE IF NOT EXISTS `PropertyType_Audit` (
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
-- Initial audit table rows for PropertyType 
--

INSERT INTO PropertyType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codePropertyType
  , name
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codePropertyType
  , name
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codePropertyType
  , OLD.name
  , OLD.isActive );
END;
$$


--
-- Audit Table For Property 
--

CREATE TABLE IF NOT EXISTS `Property_Audit` (
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
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for Property 
--

INSERT INTO Property_Audit
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
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAU_Property_FER AFTER UPDATE ON Property FOR EACH ROW
BEGIN
  INSERT INTO Property_Audit
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
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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
  , NEW.sortOrder );
END;
$$


CREATE TRIGGER TrAD_Property_FER AFTER DELETE ON Property FOR EACH ROW
BEGIN
  INSERT INTO Property_Audit
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
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For ProtocolType 
--

CREATE TABLE IF NOT EXISTS `ProtocolType_Audit` (
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
-- Initial audit table rows for ProtocolType 
--

INSERT INTO ProtocolType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeProtocolType
  , protocolType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeProtocolType
  , protocolType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeProtocolType
  , OLD.protocolType
  , OLD.isActive );
END;
$$


--
-- Audit Table For QualityControlStepEntry 
--

CREATE TABLE IF NOT EXISTS `QualityControlStepEntry_Audit` (
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
-- Initial audit table rows for QualityControlStepEntry 
--

INSERT INTO QualityControlStepEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `QualityControlStep_Audit` (
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
-- Initial audit table rows for QualityControlStep 
--

INSERT INTO QualityControlStep_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `ReactionType_Audit` (
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
-- Initial audit table rows for ReactionType 
--

INSERT INTO ReactionType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeReactionType
  , reactionType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeReactionType
  , reactionType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeReactionType
  , OLD.reactionType
  , OLD.isActive );
END;
$$


--
-- Audit Table For RequestCategoryApplication 
--

CREATE TABLE IF NOT EXISTS `RequestCategoryApplication_Audit` (
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
-- Initial audit table rows for RequestCategoryApplication 
--

INSERT INTO RequestCategoryApplication_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `RequestCategoryType_Audit` (
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
-- Initial audit table rows for RequestCategoryType 
--

INSERT INTO RequestCategoryType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `RequestCategory_Audit` (
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
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for RequestCategory 
--

INSERT INTO RequestCategory_Audit
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
  , isOwnerOnly )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , isOwnerOnly )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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
  , NEW.isOwnerOnly );
END;
$$


CREATE TRIGGER TrAU_RequestCategory_FER AFTER UPDATE ON RequestCategory FOR EACH ROW
BEGIN
  INSERT INTO RequestCategory_Audit
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
  , isOwnerOnly )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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
  , NEW.isOwnerOnly );
END;
$$


CREATE TRIGGER TrAD_RequestCategory_FER AFTER DELETE ON RequestCategory FOR EACH ROW
BEGIN
  INSERT INTO RequestCategory_Audit
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
  , isOwnerOnly )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
  , OLD.isOwnerOnly );
END;
$$


--
-- Audit Table For RequestCollaborator 
--

CREATE TABLE IF NOT EXISTS `RequestCollaborator_Audit` (
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
-- Initial audit table rows for RequestCollaborator 
--

INSERT INTO RequestCollaborator_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idRequest
  , idAppUser
  , canUploadData
  , canUpdate )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idRequest
  , OLD.idAppUser
  , OLD.canUploadData
  , OLD.canUpdate );
END;
$$


--
-- Audit Table For RequestHybridization 
--

CREATE TABLE IF NOT EXISTS `RequestHybridization_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for RequestHybridization 
--

INSERT INTO RequestHybridization_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idHybridization )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idRequest
  , idHybridization )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idRequest
  , idHybridization )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idRequest
  , idHybridization )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idRequest
  , OLD.idHybridization );
END;
$$


--
-- Audit Table For RequestSeqLibTreatment 
--

CREATE TABLE IF NOT EXISTS `RequestSeqLibTreatment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for RequestSeqLibTreatment 
--

INSERT INTO RequestSeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idRequest
  , idSeqLibTreatment )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idRequest
  , idSeqLibTreatment )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idRequest
  , OLD.idSeqLibTreatment );
END;
$$


--
-- Audit Table For RequestStatus 
--

CREATE TABLE IF NOT EXISTS `RequestStatus_Audit` (
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
-- Initial audit table rows for RequestStatus 
--

INSERT INTO RequestStatus_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRequestStatus
  , requestStatus
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeRequestStatus
  , requestStatus
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeRequestStatus
  , OLD.requestStatus
  , OLD.isActive );
END;
$$


--
-- Audit Table For RequestToTopic 
--

CREATE TABLE IF NOT EXISTS `RequestToTopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for RequestToTopic 
--

INSERT INTO RequestToTopic_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTopic
  , idRequest )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idTopic
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idTopic
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idTopic
  , idRequest )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idTopic
  , OLD.idRequest );
END;
$$


--
-- Audit Table For Request 
--

CREATE TABLE IF NOT EXISTS `Request_Audit` (
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
 ,`materialQuoteNumber`  varchar(50)  NULL DEFAULT NULL
 ,`quoteReceivedDate`  datetime  NULL DEFAULT NULL
 ,`uuid`  varchar(36)  NULL DEFAULT NULL
 ,`codeDNAPrepType`  varchar(10)  NULL DEFAULT NULL
 ,`bioinformaticsAssist`  char(1)  NULL DEFAULT NULL
 ,`hasPrePooledLibraries`  char(1)  NULL DEFAULT NULL
 ,`numPrePooledTubes`  int(10)  NULL DEFAULT NULL
 ,`codeRNAPrepType`  varchar(10)  NULL DEFAULT NULL
 ,`includeBisulfideConversion`  char(1)  NULL DEFAULT NULL
 ,`includeQubitConcentration`  char(1)  NULL DEFAULT NULL
 ,`alignToGenomeBuild`  char(1)  NULL DEFAULT NULL
 ,`adminNotes`  varchar(5000)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for Request 
--

INSERT INTO Request_Audit
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
  , materialQuoteNumber
  , quoteReceivedDate
  , uuid
  , codeDNAPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , codeRNAPrepType
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , materialQuoteNumber
  , quoteReceivedDate
  , uuid
  , codeDNAPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , codeRNAPrepType
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes
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
  , materialQuoteNumber
  , quoteReceivedDate
  , uuid
  , codeDNAPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , codeRNAPrepType
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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
  , NEW.materialQuoteNumber
  , NEW.quoteReceivedDate
  , NEW.uuid
  , NEW.codeDNAPrepType
  , NEW.bioinformaticsAssist
  , NEW.hasPrePooledLibraries
  , NEW.numPrePooledTubes
  , NEW.codeRNAPrepType
  , NEW.includeBisulfideConversion
  , NEW.includeQubitConcentration
  , NEW.alignToGenomeBuild
  , NEW.adminNotes );
END;
$$


CREATE TRIGGER TrAU_Request_FER AFTER UPDATE ON Request FOR EACH ROW
BEGIN
  INSERT INTO Request_Audit
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
  , materialQuoteNumber
  , quoteReceivedDate
  , uuid
  , codeDNAPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , codeRNAPrepType
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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
  , NEW.materialQuoteNumber
  , NEW.quoteReceivedDate
  , NEW.uuid
  , NEW.codeDNAPrepType
  , NEW.bioinformaticsAssist
  , NEW.hasPrePooledLibraries
  , NEW.numPrePooledTubes
  , NEW.codeRNAPrepType
  , NEW.includeBisulfideConversion
  , NEW.includeQubitConcentration
  , NEW.alignToGenomeBuild
  , NEW.adminNotes );
END;
$$


CREATE TRIGGER TrAD_Request_FER AFTER DELETE ON Request FOR EACH ROW
BEGIN
  INSERT INTO Request_Audit
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
  , materialQuoteNumber
  , quoteReceivedDate
  , uuid
  , codeDNAPrepType
  , bioinformaticsAssist
  , hasPrePooledLibraries
  , numPrePooledTubes
  , codeRNAPrepType
  , includeBisulfideConversion
  , includeQubitConcentration
  , alignToGenomeBuild
  , adminNotes )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
  , OLD.materialQuoteNumber
  , OLD.quoteReceivedDate
  , OLD.uuid
  , OLD.codeDNAPrepType
  , OLD.bioinformaticsAssist
  , OLD.hasPrePooledLibraries
  , OLD.numPrePooledTubes
  , OLD.codeRNAPrepType
  , OLD.includeBisulfideConversion
  , OLD.includeQubitConcentration
  , OLD.alignToGenomeBuild
  , OLD.adminNotes );
END;
$$


--
-- Audit Table For RNAPrepType 
--

CREATE TABLE IF NOT EXISTS `RNAPrepType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRNAPrepType`  varchar(10)  NULL DEFAULT NULL
 ,`rnaPrepType`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for RNAPrepType 
--

INSERT INTO RNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRNAPrepType
  , rnaPrepType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , codeRNAPrepType
  , rnaPrepType
  , isActive
  FROM RNAPrepType
  WHERE NOT EXISTS(SELECT * FROM RNAPrepType_Audit)
$$

--
-- Audit Triggers For RNAPrepType 
--


CREATE TRIGGER TrAI_RNAPrepType_FER AFTER INSERT ON RNAPrepType FOR EACH ROW
BEGIN
  INSERT INTO RNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRNAPrepType
  , rnaPrepType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.codeRNAPrepType
  , NEW.rnaPrepType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_RNAPrepType_FER AFTER UPDATE ON RNAPrepType FOR EACH ROW
BEGIN
  INSERT INTO RNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRNAPrepType
  , rnaPrepType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.codeRNAPrepType
  , NEW.rnaPrepType
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_RNAPrepType_FER AFTER DELETE ON RNAPrepType FOR EACH ROW
BEGIN
  INSERT INTO RNAPrepType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeRNAPrepType
  , rnaPrepType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeRNAPrepType
  , OLD.rnaPrepType
  , OLD.isActive );
END;
$$


--
-- Audit Table For SampleDropOffLocation 
--

CREATE TABLE IF NOT EXISTS `SampleDropOffLocation_Audit` (
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
-- Initial audit table rows for SampleDropOffLocation 
--

INSERT INTO SampleDropOffLocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive
  FROM SampleDropOffLocation
  WHERE NOT EXISTS(SELECT * FROM SampleDropOffLocation_Audit)
$$

--
-- Audit Triggers For SampleDropOffLocation 
--


CREATE TRIGGER TrAI_SampleDropOffLocation_FER AFTER INSERT ON SampleDropOffLocation FOR EACH ROW
BEGIN
  INSERT INTO SampleDropOffLocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idSampleDropOffLocation
  , NEW.sampleDropOffLocation
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_SampleDropOffLocation_FER AFTER UPDATE ON SampleDropOffLocation FOR EACH ROW
BEGIN
  INSERT INTO SampleDropOffLocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idSampleDropOffLocation
  , NEW.sampleDropOffLocation
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_SampleDropOffLocation_FER AFTER DELETE ON SampleDropOffLocation FOR EACH ROW
BEGIN
  INSERT INTO SampleDropOffLocation_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleDropOffLocation
  , sampleDropOffLocation
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSampleDropOffLocation
  , OLD.sampleDropOffLocation
  , OLD.isActive );
END;
$$


--
-- Audit Table For SampleExperimentFile 
--

CREATE TABLE IF NOT EXISTS `SampleExperimentFile_Audit` (
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
-- Initial audit table rows for SampleExperimentFile 
--

INSERT INTO SampleExperimentFile_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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
  , idSampleExperimentFile
  , idSample
  , idExpFileRead1
  , idExpFileRead2
  , seqRunNumber
  FROM SampleExperimentFile
  WHERE NOT EXISTS(SELECT * FROM SampleExperimentFile_Audit)
$$

--
-- Audit Triggers For SampleExperimentFile 
--


CREATE TRIGGER TrAI_SampleExperimentFile_FER AFTER INSERT ON SampleExperimentFile FOR EACH ROW
BEGIN
  INSERT INTO SampleExperimentFile_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idSampleExperimentFile
  , NEW.idSample
  , NEW.idExpFileRead1
  , NEW.idExpFileRead2
  , NEW.seqRunNumber );
END;
$$


CREATE TRIGGER TrAU_SampleExperimentFile_FER AFTER UPDATE ON SampleExperimentFile FOR EACH ROW
BEGIN
  INSERT INTO SampleExperimentFile_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idSampleExperimentFile
  , NEW.idSample
  , NEW.idExpFileRead1
  , NEW.idExpFileRead2
  , NEW.seqRunNumber );
END;
$$


CREATE TRIGGER TrAD_SampleExperimentFile_FER AFTER DELETE ON SampleExperimentFile FOR EACH ROW
BEGIN
  INSERT INTO SampleExperimentFile_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSampleExperimentFile
  , OLD.idSample
  , OLD.idExpFileRead1
  , OLD.idExpFileRead2
  , OLD.seqRunNumber );
END;
$$


--
-- Audit Table For SampleFileType 
--

CREATE TABLE IF NOT EXISTS `SampleFileType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSampleFileType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for SampleFileType 
--

INSERT INTO SampleFileType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSampleFileType
  , description )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , codeSampleFileType
  , description
  FROM SampleFileType
  WHERE NOT EXISTS(SELECT * FROM SampleFileType_Audit)
$$

--
-- Audit Triggers For SampleFileType 
--


CREATE TRIGGER TrAI_SampleFileType_FER AFTER INSERT ON SampleFileType FOR EACH ROW
BEGIN
  INSERT INTO SampleFileType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSampleFileType
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.codeSampleFileType
  , NEW.description );
END;
$$


CREATE TRIGGER TrAU_SampleFileType_FER AFTER UPDATE ON SampleFileType FOR EACH ROW
BEGIN
  INSERT INTO SampleFileType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSampleFileType
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.codeSampleFileType
  , NEW.description );
END;
$$


CREATE TRIGGER TrAD_SampleFileType_FER AFTER DELETE ON SampleFileType FOR EACH ROW
BEGIN
  INSERT INTO SampleFileType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSampleFileType
  , description )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeSampleFileType
  , OLD.description );
END;
$$


--
-- Audit Table For SamplePrepMethod 
--

CREATE TABLE IF NOT EXISTS `SamplePrepMethod_Audit` (
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
-- Initial audit table rows for SamplePrepMethod 
--

INSERT INTO SamplePrepMethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive
  FROM SamplePrepMethod
  WHERE NOT EXISTS(SELECT * FROM SamplePrepMethod_Audit)
$$

--
-- Audit Triggers For SamplePrepMethod 
--


CREATE TRIGGER TrAI_SamplePrepMethod_FER AFTER INSERT ON SamplePrepMethod FOR EACH ROW
BEGIN
  INSERT INTO SamplePrepMethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idSamplePrepMethod
  , NEW.samplePrepMethod
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_SamplePrepMethod_FER AFTER UPDATE ON SamplePrepMethod FOR EACH ROW
BEGIN
  INSERT INTO SamplePrepMethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idSamplePrepMethod
  , NEW.samplePrepMethod
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_SamplePrepMethod_FER AFTER DELETE ON SamplePrepMethod FOR EACH ROW
BEGIN
  INSERT INTO SamplePrepMethod_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSamplePrepMethod
  , samplePrepMethod
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSamplePrepMethod
  , OLD.samplePrepMethod
  , OLD.isActive );
END;
$$


--
-- Audit Table For SampleSource 
--

CREATE TABLE IF NOT EXISTS `SampleSource_Audit` (
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
-- Initial audit table rows for SampleSource 
--

INSERT INTO SampleSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleSource
  , sampleSource
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idSampleSource
  , sampleSource
  , isActive
  FROM SampleSource
  WHERE NOT EXISTS(SELECT * FROM SampleSource_Audit)
$$

--
-- Audit Triggers For SampleSource 
--


CREATE TRIGGER TrAI_SampleSource_FER AFTER INSERT ON SampleSource FOR EACH ROW
BEGIN
  INSERT INTO SampleSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idSampleSource
  , NEW.sampleSource
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAU_SampleSource_FER AFTER UPDATE ON SampleSource FOR EACH ROW
BEGIN
  INSERT INTO SampleSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idSampleSource
  , NEW.sampleSource
  , NEW.isActive );
END;
$$


CREATE TRIGGER TrAD_SampleSource_FER AFTER DELETE ON SampleSource FOR EACH ROW
BEGIN
  INSERT INTO SampleSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleSource
  , sampleSource
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSampleSource
  , OLD.sampleSource
  , OLD.isActive );
END;
$$


--
-- Audit Table For SampleTypeRequestCategory 
--

CREATE TABLE IF NOT EXISTS `SampleTypeRequestCategory_Audit` (
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
-- Initial audit table rows for SampleTypeRequestCategory 
--

INSERT INTO SampleTypeRequestCategory_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idSampleTypeRequestCategory
  , idSampleType
  , codeRequestCategory )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSampleTypeRequestCategory
  , OLD.idSampleType
  , OLD.codeRequestCategory );
END;
$$


--
-- Audit Table For SampleType 
--

CREATE TABLE IF NOT EXISTS `SampleType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`sampleType`  varchar(50)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for SampleType 
--

INSERT INTO SampleType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType
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
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
  , NEW.idSampleType
  , NEW.sampleType
  , NEW.sortOrder
  , NEW.isActive
  , NEW.codeNucleotideType );
END;
$$


CREATE TRIGGER TrAU_SampleType_FER AFTER UPDATE ON SampleType FOR EACH ROW
BEGIN
  INSERT INTO SampleType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
  , NEW.idSampleType
  , NEW.sampleType
  , NEW.sortOrder
  , NEW.isActive
  , NEW.codeNucleotideType );
END;
$$


CREATE TRIGGER TrAD_SampleType_FER AFTER DELETE ON SampleType FOR EACH ROW
BEGIN
  INSERT INTO SampleType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSampleType
  , sampleType
  , sortOrder
  , isActive
  , codeNucleotideType )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSampleType
  , OLD.sampleType
  , OLD.sortOrder
  , OLD.isActive
  , OLD.codeNucleotideType );
END;
$$


--
-- Audit Table For Sample 
--

CREATE TABLE IF NOT EXISTS `Sample_Audit` (
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
-- Initial audit table rows for Sample 
--

INSERT INTO Sample_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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


CREATE TRIGGER TrAU_Sample_FER AFTER UPDATE ON Sample FOR EACH ROW
BEGIN
  INSERT INTO Sample_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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


CREATE TRIGGER TrAD_Sample_FER AFTER DELETE ON Sample FOR EACH ROW
BEGIN
  INSERT INTO Sample_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
-- Audit Table For ScanProtocol 
--

CREATE TABLE IF NOT EXISTS `ScanProtocol_Audit` (
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
-- Initial audit table rows for ScanProtocol 
--

INSERT INTO ScanProtocol_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `SealType_Audit` (
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
-- Initial audit table rows for SealType 
--

INSERT INTO SealType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSealType
  , sealType
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeSealType
  , sealType
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeSealType
  , OLD.sealType
  , OLD.isActive );
END;
$$


--
-- Audit Table For Segment 
--

CREATE TABLE IF NOT EXISTS `Segment_Audit` (
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
-- Initial audit table rows for Segment 
--

INSERT INTO Segment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
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

CREATE TABLE IF NOT EXISTS `SeqLibProtocolApplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for SeqLibProtocolApplication 
--

INSERT INTO SeqLibProtocolApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibProtocol
  , codeApplication )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idSeqLibProtocol
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSeqLibProtocol
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For SeqLibProtocol 
--

CREATE TABLE IF NOT EXISTS `SeqLibProtocol_Audit` (
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
-- Initial audit table rows for SeqLibProtocol 
--

INSERT INTO SeqLibProtocol_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `SeqLibTreatment_Audit` (
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
-- Initial audit table rows for SeqLibTreatment 
--

INSERT INTO SeqLibTreatment_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idSeqLibTreatment
  , seqLibTreatment
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSeqLibTreatment
  , OLD.seqLibTreatment
  , OLD.isActive );
END;
$$


--
-- Audit Table For SeqRunType 
--

CREATE TABLE IF NOT EXISTS `SeqRunType_Audit` (
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
-- Initial audit table rows for SeqRunType 
--

INSERT INTO SeqRunType_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idSeqRunType
  , seqRunType
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSeqRunType
  , OLD.seqRunType
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For SequenceLane 
--

CREATE TABLE IF NOT EXISTS `SequenceLane_Audit` (
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
-- Initial audit table rows for SequenceLane 
--

INSERT INTO SequenceLane_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `SequencingControl_Audit` (
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
-- Initial audit table rows for SequencingControl 
--

INSERT INTO SequencingControl_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idSequencingControl
  , sequencingControl
  , isActive
  , idAppUser )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSequencingControl
  , OLD.sequencingControl
  , OLD.isActive
  , OLD.idAppUser );
END;
$$


--
-- Audit Table For SequencingPlatform 
--

CREATE TABLE IF NOT EXISTS `SequencingPlatform_Audit` (
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
-- Initial audit table rows for SequencingPlatform 
--

INSERT INTO SequencingPlatform_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeSequencingPlatform
  , sequencingPlatform
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeSequencingPlatform
  , OLD.sequencingPlatform
  , OLD.isActive );
END;
$$


--
-- Audit Table For SlideDesign 
--

CREATE TABLE IF NOT EXISTS `SlideDesign_Audit` (
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
-- Initial audit table rows for SlideDesign 
--

INSERT INTO SlideDesign_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `SlideProductApplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for SlideProductApplication 
--

INSERT INTO SlideProductApplication_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlideProduct
  , codeApplication )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idSlideProduct
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idSlideProduct
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idSlideProduct
  , codeApplication )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSlideProduct
  , OLD.codeApplication );
END;
$$


--
-- Audit Table For SlideProduct 
--

CREATE TABLE IF NOT EXISTS `SlideProduct_Audit` (
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
-- Initial audit table rows for SlideProduct 
--

INSERT INTO SlideProduct_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `SlideSource_Audit` (
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
-- Initial audit table rows for SlideSource 
--

INSERT INTO SlideSource_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeSlideSource
  , slideSource
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeSlideSource
  , OLD.slideSource
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For Slide 
--

CREATE TABLE IF NOT EXISTS `Slide_Audit` (
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
-- Initial audit table rows for Slide 
--

INSERT INTO Slide_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idSlide
  , barcode
  , idSlideDesign
  , slideName )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idSlide
  , OLD.barcode
  , OLD.idSlideDesign
  , OLD.slideName );
END;
$$


--
-- Audit Table For State 
--

CREATE TABLE IF NOT EXISTS `State_Audit` (
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
-- Initial audit table rows for State 
--

INSERT INTO State_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeState
  , state
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeState
  , state
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeState
  , state
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeState
  , state
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeState
  , OLD.state
  , OLD.isActive );
END;
$$


--
-- Audit Table For Step 
--

CREATE TABLE IF NOT EXISTS `Step_Audit` (
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
-- Initial audit table rows for Step 
--

INSERT INTO Step_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeStep
  , step
  , isActive
  , sortOrder )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeStep
  , step
  , isActive
  , sortOrder )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeStep
  , OLD.step
  , OLD.isActive
  , OLD.sortOrder );
END;
$$


--
-- Audit Table For SubmissionInstruction 
--

CREATE TABLE IF NOT EXISTS `SubmissionInstruction_Audit` (
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
-- Initial audit table rows for SubmissionInstruction 
--

INSERT INTO SubmissionInstruction_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `Topic_Audit` (
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
-- Initial audit table rows for Topic 
--

INSERT INTO Topic_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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

CREATE TABLE IF NOT EXISTS `TreatmentEntry_Audit` (
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
-- Initial audit table rows for TreatmentEntry 
--

INSERT INTO TreatmentEntry_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idTreatmentEntry
  , treatment
  , idSample
  , otherLabel )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idTreatmentEntry
  , OLD.treatment
  , OLD.idSample
  , OLD.otherLabel );
END;
$$


--
-- Audit Table For UnloadDataTrack 
--

CREATE TABLE IF NOT EXISTS `UnloadDataTrack_Audit` (
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
-- Initial audit table rows for UnloadDataTrack 
--

INSERT INTO UnloadDataTrack_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idUnloadDataTrack
  , typeName
  , idAppUser
  , idGenomeBuild )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idUnloadDataTrack
  , OLD.typeName
  , OLD.idAppUser
  , OLD.idGenomeBuild );
END;
$$


--
-- Audit Table For UserPermissionKind 
--

CREATE TABLE IF NOT EXISTS `UserPermissionKind_Audit` (
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
-- Initial audit table rows for UserPermissionKind 
--

INSERT INTO UserPermissionKind_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeUserPermissionKind
  , userPermissionKind
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeUserPermissionKind
  , OLD.userPermissionKind
  , OLD.isActive );
END;
$$


--
-- Audit Table For Vendor 
--

CREATE TABLE IF NOT EXISTS `Vendor_Audit` (
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
-- Initial audit table rows for Vendor 
--

INSERT INTO Vendor_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , idVendor
  , vendorName
  , description
  , isActive )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , idVendor
  , vendorName
  , description
  , isActive )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.idVendor
  , OLD.vendorName
  , OLD.description
  , OLD.isActive );
END;
$$


--
-- Audit Table For Visibility 
--

CREATE TABLE IF NOT EXISTS `Visibility_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`visibility`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Initial audit table rows for Visibility 
--

INSERT INTO Visibility_Audit
  ( AuditAppuser
  , AuditOperation
  , AuditSystemUser
  , AuditOperationDate
  , codeVisibility
  , visibility )
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  , codeVisibility
  , visibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
  , NOW()
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
  , codeVisibility
  , visibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
  , NOW()
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
  , codeVisibility
  , visibility )
  VALUES
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
  , NOW()
  , OLD.codeVisibility
  , OLD.visibility );
END;
$$


--
-- Audit Table For WorkItem 
--

CREATE TABLE IF NOT EXISTS `WorkItem_Audit` (
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
-- Initial audit table rows for WorkItem 
--

INSERT INTO WorkItem_Audit
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
  SELECT
  'No Context'
  , 'L'
  , USER()
  , NOW()
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
  FROM WorkItem
  WHERE NOT EXISTS(SELECT * FROM WorkItem_Audit)
$$

--
-- Audit Triggers For WorkItem 
--


CREATE TRIGGER TrAI_WorkItem_FER AFTER INSERT ON WorkItem FOR EACH ROW
BEGIN
  INSERT INTO WorkItem_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'I'
  , USER()
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


CREATE TRIGGER TrAU_WorkItem_FER AFTER UPDATE ON WorkItem FOR EACH ROW
BEGIN
  INSERT INTO WorkItem_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'U'
  , USER()
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


CREATE TRIGGER TrAD_WorkItem_FER AFTER DELETE ON WorkItem FOR EACH ROW
BEGIN
  INSERT INTO WorkItem_Audit
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
  ( CASE WHEN @userName IS NULL THEN 'No Context' else @userName end
  , 'D'
  , USER()
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
