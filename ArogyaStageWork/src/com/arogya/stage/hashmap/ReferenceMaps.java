package com.arogya.stage.hashmap;

import java.util.HashMap;

public class ReferenceMaps {

	public static HashMap<String, String> obxMap = new HashMap<String, String>();

	public ReferenceMaps() {

		obxMap.put("OBX.0>", "Segment_type>");

		obxMap.put("OBX.1>", "Sequence_No>");
		obxMap.put("OBX.2>", "Value_type>");
		obxMap.put("OBX.3>", "Observation_identifier>");
		obxMap.put("OBX.3.1>", "Observation_Test_id>");
		obxMap.put("OBX.3.2>", "Observation_Text>");
		obxMap.put("OBX.3.3>", "Observation_Coding_System>");
		obxMap.put("OBX.4", "Observation_sub_id");
		obxMap.put("OBX.5", "Observation_value");
		obxMap.put("OBX.6", "Result_units_of_measurement");
		obxMap.put("OBX.7", "Result_unit_reference_range");
		obxMap.put("OBX.8", "Abnormal_flags");
		obxMap.put("OBX.9", "Probability");
		obxMap.put("OBX.10", "Nature_of_abnormal_test");
		obxMap.put("OBX.11", "Observation_result_status");
		obxMap.put("OBX.12", "Effective_date_of_last_normal_observation");
		obxMap.put("OBX.13", "User-defined_access_checks");
		obxMap.put("OBX.14", "Observation_date_and_time");
		obxMap.put("OBX.15", "Producer_id");
		obxMap.put("OBX.16", "Responsible_observer");
		obxMap.put("OBX.17", "Observation_method");
		obxMap.put("OBX.18", "Observation_Equip_Identifier");
		obxMap.put("OBX.19", "Observation_Analysis_date_and_time");

		obxMap.put("OBR.0>", "Segment_type");
		obxMap.put("OBR.1", "Sequence_number");
		obxMap.put("OBR.2", "Placer_order_number");
		obxMap.put("OBR.3", "Filler_order_number");
		obxMap.put("OBR.4", "Universal_service_ID");
		obxMap.put("OBR.5", "Priority");
		obxMap.put("OBR.6", "Requested_date_and_time");
		obxMap.put("OBR.7", "Observation_date_and_time");
		obxMap.put("OBR.8", "Observation_end_date_and_time");
		obxMap.put("OBR.9", "Collection_volume");
		obxMap.put("OBR.10", "Collector_identifier");
		obxMap.put("OBR.11", "Action_code");
		obxMap.put("OBR.12", "Danger_code");
		obxMap.put("OBR.13", "Relevant_clinical_information");
		obxMap.put("OBR.14", "Specimen_received_date_and_time");
		obxMap.put("OBR.15", "Specimen_source");
		obxMap.put("OBR.16", "Ordering_provider");
		obxMap.put("OBR.17", "Order_callback_phone_number");
		obxMap.put("OBR.18", "Placer_field_#1");
		obxMap.put("OBR.19", "Placer_field_#2");
		obxMap.put("OBR.20", "Filler_1");
		obxMap.put("OBR.21", "Filler_2");
		obxMap.put("OBR.22", "Result_reported_date_and_time");
		obxMap.put("OBR.23", "Charge_to_practice");
		obxMap.put("OBR.24", "Diagnostic_serv_sect_ID");
		obxMap.put("OBR.25", "Test_status");

		obxMap.put("MSH.0", "Segment_identifier");
		obxMap.put("MSH.1", "Field_delimiter");
		obxMap.put("MSH.2", "Encoding_characters");
		obxMap.put("MSH.3", "Sending_application");
		obxMap.put("MSH.4", "Sending_facility");
		obxMap.put("MSH.5", "Receiving_application");
		obxMap.put("MSH.6", "Receiving_facility");
		obxMap.put("MSH.7", "Message_date_and_time");
		obxMap.put("MSH.8", "Security");
		obxMap.put("MSH.9", "Message_type");
		obxMap.put("MSH.10", "Message_control_ID");
		obxMap.put("MSH.11", "Processing_ID");
		obxMap.put("MSH.12", "HL7_version");
		obxMap.put("MSH.13", "Sequence_number");
		obxMap.put("MSH.14", "Continuation_pointer");
		obxMap.put("MSH.15", "Accept_acknowledgement_type");
		obxMap.put("MSH.16", "Application_acknowledgement_type");
		obxMap.put("MSH.17", "Country_code");
		obxMap.put("MSH.18", "Character_set");
		obxMap.put("MSH.19", "Principle_language_of_message");

		obxMap.put("PID.0", "Segment_type_ID");
		obxMap.put("PID.1", "Sequence_number");
		obxMap.put("PID.2", "External_patient_ID");
		obxMap.put("PID.3", "Patient_identifier_list");
		obxMap.put("PID.4", "Alternate_patient_ID");
		obxMap.put("PID.5", "Patient_name");
		obxMap.put("PID.6", "Mother_maiden_name");
		obxMap.put("PID.7", "Patient_date_of_birth");
		obxMap.put("PID.8", "Patient_gender");
		obxMap.put("PID.9", "Patient_alias");
		obxMap.put("PID.10", "Patient_race");
		obxMap.put("PID.11", "Patient_address");
		obxMap.put("PID.12", "Patient_county_code");
		obxMap.put("PID.13", "Patient_home_phone_number");
		obxMap.put("PID.13", "Patient_home_phone_number");

		obxMap.put("PV1.0", "Segment_identifier");
		obxMap.put("PV1.1", "Sequence_number");
		obxMap.put("PV1.2", "Patient_class");
		obxMap.put("PV1.3", "Assigned_patient_location");
		obxMap.put("PV1.4", "Admission_Type");
		obxMap.put("PV1.5", "Pre-admit_number");
		obxMap.put("PV1.6", "Prior_patient_location");
		obxMap.put("PV1.7", "Attending_provider");
		obxMap.put("ORC.0", "Segment_type");
		obxMap.put("ORC.1", "Order_control");
		obxMap.put("ORC.2", "Placer_order_number");
		obxMap.put("ORC.3", "Filler_order_number");
		obxMap.put("ORC.4", "Placer_group_number");
		obxMap.put("ORC.5", "Order_status");
		obxMap.put("ORC.6", "Response_flag");
		obxMap.put("NTE.0", "Segment_type");
		obxMap.put("NTE.1", "Sequence_number");
		obxMap.put("NTE.2", "Comment_source");
		obxMap.put("NTE.3", "Comment");

		obxMap.put("PD1.0", "Segment_identifier");
		obxMap.put("PD1.1", "Living_Dependency");
		obxMap.put("PD1.2", "Living_1Arrangement");
		obxMap.put("PD1.3", "Patient_Primary_Facility");
		obxMap.put("PD1.4", "Patient_Primary_Care_Provider");
		obxMap.put("PD1.4.1", "ID_Number");
		obxMap.put("PD1.4.2", "Family_Name");
		obxMap.put("PD1.4.3", "Given_Name");
		obxMap.put("PD1.4.4", "Middle_Name");
		obxMap.put("PD1.4.5", "Suffix");
		obxMap.put("PD1.4.6", "Prefix");
		obxMap.put("PD1.4.7", "Degree");
		obxMap.put("PD1.4.8", "Source_Table");
		obxMap.put("PD1.4.9", "Assignment_Authority");

	}

	public HashMap<String, String> getObxMap() {
		return obxMap;
	}

}