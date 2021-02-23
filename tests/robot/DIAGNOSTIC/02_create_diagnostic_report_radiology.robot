# Copyright (c) 2021 Peter Wohlfarth (Appsfactory GmbH)
#
# This file is part of Project EHRbase
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


*** Settings ***
Resource                ${EXECDIR}/robot/_resources/suite_settings.robot

Test Setup              generic.prepare new request session    Prefer=return=representation
...															   Authorization=${AUTHORIZATION['Authorization']}
Documentation           *NOTE:* Use Regular Expressions to replace braces () as described here:
...                	    https://json-schema.org/understanding-json-schema/reference/regular_expressions.html#example \n\n
...						*Author:* Peter Wohlfarth
Force Tags              diagnostic_create    diagnostic-report    diagnostic-report-radiology    invalid    create


*** Variables ***
${randstring}                   foobar
${randinteger}                  ${12345}


*** Test Cases ***

001 Create Diagnostic Report Radiology (Invalid/Missing 'resourceType')
	[Documentation]     1. *CREATE* new an EHR record\n\n 
	...                 2. *LOAD* _create-radiology-report-normal-finding.json_\n\n
	...                 3. *UPDATE* ``Subject - Identifier - value`` with the _UUID:_ subject_id which was created in EHR record\n\n
	...                 4. *UPDATE* values for attribute ``resourceType`` \n\n
	...                 5. *POST* example JSON to observation endpoint\n\n
	...                 6. *VALIDATE* the response status \n\n                
	[Template]		    create Diagnostic Report Radiology with ehr reference
	[Tags]          	resourceType

	# FIELD/PATH					VALUE							HTTP
	# 																CODE
	$.resourceType					missing							422
	$.resourceType					${randstring}					422
	$.resourceType					EMPTY							422
	$.resourceType					${randinteger}					422



002 Create Diagnostic Report Radiology (Invalid/Missing 'category')
	[Documentation]     1. *CREATE* new an EHR record\n\n 
	...                 2. *LOAD* _create-radiology-report-normal-finding.json_\n\n
	...                 3. *UPDATE* ``Subject - Identifier - value`` with the _UUID:_ subject_id which was created in EHR record\n\n
	...                 4. *UPDATE* values for attribute ``Category`` \n\n
	...                 5. *POST* example JSON to observation endpoint\n\n
	...                 6. *VALIDATE* the response status \n\n               
	[Template]			create Diagnostic Report Radiology with ehr reference
	[Tags]              category

	# FIELD/PATH							VALUE					HTTP
	# 																CODE

	#invalid coding
	$.category[0].coding    				missing					422
	$.category[0].coding    				EMPTY					422
	$.category[0].coding					${{ [] }}				422
	$.category[0].coding					${{ {} }}				422
	$.category[0].coding					${{ [{}] }}				422

	#invalid code 0
	$.category[0].coding[0].code    		EMPTY    	    		422
	$.category[0].coding[0].code    		${randstring}	    	422
	$.category[0].coding[0].code    		${randinteger}	    	422
	$.category[0].coding[0].code 			${{ [] }}				422
	$.category[0].coding[0].code 			${{ {} }}				422
	$.category[0].coding[0].code 			${{ [{}] }}				422

	# invaild system 0
	$.category[0].coding[0].system    		EMPTY    	    		422
	$.category[0].coding[0].system    		${randstring}	    	422
	$.category[0].coding[0].system    		${randinteger}	    	422
	$.category[0].coding[0].system    		http://foobar.de      	422
	$.category[0].coding[0].system 			${{ [] }}				422
	$.category[0].coding[0].system 			${{ {} }}				422
	$.category[0].coding[0].system 			${{ [{}] }}				422



003 Create Diagnostic Report Radiology (Invalid/Missing 'code')
	[Documentation]     1. *CREATE* new an EHR record\n\n 
	...                 2. *LOAD* _create-radiology-report-normal-finding.json_\n\n
	...                 3. *UPDATE* ``Subject - Identifier - value`` with the _UUID:_ subject_id which was created in EHR record\n\n
	...                 4. *UPDATE* values for attribute ``Code`` \n\n
	...                 5. *POST* example JSON to observation endpoint\n\n
	...                 6. *VALIDATE* the response status \n\n                 
	[Template]			create Diagnostic Report Radiology with ehr reference
	[Tags]              code

	# FIELD/PATH							VALUE					HTTP
	# 																CODE

	# invalid code
	$.code									missing					422
	$.code									EMPTY					422
	$.code									${{ [] }}				422
	$.code									${{ {} }}				422
	$.code									${{ [{}] }}				422

	# invalid coding
	$.code.coding   	 					missing					422
	$.code.coding	    					EMPTY					422
	$.code.coding							${{ [] }}				422
	$.code.coding							${{ {} }}				422
	$.code.coding							${{ [{}] }}				422

	# invalid Code Coding 0 System
	$.code.coding[0].system					EMPTY					422
	$.code.coding[0].system					${randstring}			422
	$.code.coding[0].system					${randinteger}			422
	$.code.coding[0].system      			${{ [] }}				422
	$.code.coding[0].system      			${{ {} }}				422
	$.code.coding[0].system      			${{ [{}] }}				422

	# invalid Code Coding 0 Code
	$.code.coding[0].code					EMPTY					422
	$.code.coding[0].code					${randinteger}			422
	$.code.coding[0].code      			    ${{ [] }}				422
	$.code.coding[0].code      			    ${{ {} }}				422
	$.code.coding[0].code      			    ${{ [{}] }}				422



004 Create Diagnostic Report Radiology (Invalid/Missing 'conclusion')
	[Documentation]     1. *CREATE* new an EHR record\n\n 
	...                 2. *LOAD* _create-radiology-report-normal-finding.json_\n\n
	...                 3. *UPDATE* ``Subject - Identifier - value`` with the _UUID:_ subject_id which was created in EHR record\n\n
	...                 4. *UPDATE* values for attribute ``PARAMETER`` \n\n
	...                 5. *POST* example JSON to observation endpoint\n\n
	...                 6. *VALIDATE* the response status \n\n                
	[Template]		    create Diagnostic Report Radiology with ehr reference
	[Tags]          	conclusion    not-ready    not-ready_bug

	# FIELD/PATH					VALUE							HTTP
	# 																CODE
	#$.conclusion					missing							422
	$.conclusion					${randstring}					422
	$.conclusion					EMPTY							422
	$.conclusion					${randinteger}					422
	


005 Create Diagnostic Report Radiology (Invalid/Missing 'conclusionCode')
	[Documentation]     1. *CREATE* new an EHR record\n\n 
	...                 2. *LOAD* _create-radiology-report-normal-finding.json_\n\n
	...                 3. *UPDATE* ``Subject - Identifier - value`` with the _UUID:_ subject_id which was created in EHR record\n\n
	...                 4. *UPDATE* values for attribute ``PARAMETER`` \n\n
	...                 5. *POST* example JSON to observation endpoint\n\n
	...                 6. *VALIDATE* the response status \n\n                
	[Template]		    create Diagnostic Report Radiology with ehr reference
	[Tags]          	conclusionCode    not-ready    not-ready_bug

	# FIELD/PATH							VALUE					HTTP
	# 																CODE

	# invalid conclusionCode
	$.conclusionCode						missing					422
	$.conclusionCode						EMPTY					422
	$.conclusionCode						${{ [] }}				422
	$.conclusionCode						${{ {} }}				422
	$.conclusionCode						${{ [{}] }}				422

	# invalid conclusionCode coding
	$.conclusionCode[0].coding   	 		missing					422
	$.conclusionCode[0].coding	    		EMPTY					422
	$.conclusionCode[0].coding				${{ [] }}				422
	$.conclusionCode[0].coding				${{ {} }}				422
	$.conclusionCode[0].coding				${{ [{}] }}				422

	# invalid conclusionCode Coding 0 System
	$.conclusionCode[0].coding[0].system	EMPTY					422
	$.conclusionCode[0].coding[0].system	${randstring}			422
	$.conclusionCode[0].coding[0].system	${randinteger}			422
	$.conclusionCode[0].coding[0].system    ${{ [] }}				422
	$.conclusionCode[0].coding[0].system    ${{ {} }}				422
	$.conclusionCode[0].coding[0].system    ${{ [{}] }}				422

	# invalid conclusionCode Coding 0 Code
	#$.conclusionCode[0].coding[0].code		EMPTY					422
	$.conclusionCode[0].coding[0].code		${randinteger}			422
	$.conclusionCode[0].coding[0].code      ${{ [] }}				422
	$.conclusionCode[0].coding[0].code      ${{ {} }}				422
	$.conclusionCode[0].coding[0].code      ${{ [{}] }}				422




006 Create Diagnostic Report Radiology (Invalid/Missing 'subject')
    [Documentation]     1. *LOAD* _create-radiology-report-normal-finding.json_ \n\n
	...                 2. *UPDATE* values for attribute ``Subject`` \n\n
    ...                 3. *POST* example JSON to observation endpoint \n\n
	...                 4. *VALIDATE* the response status              
	[Template]		    create Diagnostic Report Radiology w/o ehr reference 
    [Tags]          	subject

	# FIELD/PATH					VALUE							HTTP
	# 																CODE
    # invalid cases for value
    $.subject.identifier.value		missing							422
    $.subject.identifier.value		foobar							422
    $.subject.identifier.value		${EMPTY}						422
    $.subject.identifier.value		${{ [] }}						422
    $.subject.identifier.value		${{ {} }}						422
    $.subject.identifier.value		${123}							422

	# invalid cases for system
    $.subject.identifier.system		foobar							422
    $.subject.identifier.system		${EMPTY}						422
    $.subject.identifier.system		${{ [] }}						422
    $.subject.identifier.system		${{ {} }}						422
    $.subject.identifier.system		${123}							422

	# invalid cases for identifier
    $.subject.identifier			missing							422
    $.subject.identifier			${EMPTY}						422
    $.subject.identifier			${{ [] }}						422
    $.subject.identifier			${{ {} }}						422
    $.subject.identifier			${123}							422

	# invalid cases for subject
    $.subject						missing							422
    $.subject						${EMPTY}						422
    $.subject						${{ [] }}						422
    $.subject						${{ {} }}						422
    $.subject						${123}							422
	
	# comment: random uuid												
    $.subject.identifier.value      ${{str(uuid.uuid4())}}    		422



#--------------------------------------------------------------------------------------------------------------------------------------------------------------------
# BUG TRACE
#--------------------------------------------------------------------------------------------------------------------------------------------------------------------
BUG TRACE 01 Create Diagnostic Report Radiology (Invalid/Missing 'conclusion')
	[Documentation]		Belongs to TC 004! Remove separation when it's fixed!
	[Template]			create Diagnostic Report Radiology with ehr reference
    [Tags]              conclusion    not-ready    not-ready_bug

	# FIELD/PATH							VALUE					HTTP
	# 																CODE
	
	# missing attribute
   $.conclusion								missing					422

BUG TRACE 02 Create Diagnostic Report Radiology (Invalid/Missing 'conclusionCode')
	[Documentation]		Belongs to TC 005! Remove separation when it's fixed!
	[Template]			create Diagnostic Report Radiology with ehr reference
    [Tags]              conclusionCode    not-ready    not-ready_bug

	# FIELD/PATH							VALUE					HTTP
	# 																CODE
	
	# empty attribute
   $.conclusionCode[0].coding[0].code		EMPTY					422

#--------------------------------------------------------------------------------------------------------------------------------------------------------------------

*** Keywords ***

# oooo    oooo oooooooooooo oooooo   oooo oooooo   oooooo     oooo   .oooooo.   ooooooooo.   oooooooooo.    .oooooo..o
# `888   .8P'  `888'     `8  `888.   .8'   `888.    `888.     .8'   d8P'  `Y8b  `888   `Y88. `888'   `Y8b  d8P'    `Y8
#  888  d8'     888           `888. .8'     `888.   .8888.   .8'   888      888  888   .d88'  888      888 Y88bo.
#  88888[       888oooo8       `888.8'       `888  .8'`888. .8'    888      888  888ooo88P'   888      888  `"Y8888o.
#  888`88b.     888    "        `888'         `888.8'  `888.8'     888      888  888`88b.     888      888      `"Y88b
#  888  `88b.   888       o      888           `888'    `888'      `88b    d88'  888  `88b.   888     d88' oo     .d8P
# o888o  o888o o888ooooood8     o888o           `8'      `8'        `Y8bood8P'  o888o  o888o o888bood8P'   8""88888P'
#
# [ HIGH LEVEL KEYWORDS ]



create Diagnostic Report Radiology with ehr reference
	[Arguments]         ${json_path}        ${value}                 ${http_status_code}

						ehr.create new ehr                      000_ehr_status.json
	${payload}=    		generate payload from example json    ${json_path}    ${value}
						diagnostic.POST /Diagnostic  Diagnostic Report Radiology    ${payload}
						observation.validate response - 422 (w/o error message)    ${http_status_code}


create Diagnostic Report Radiology w/o ehr reference    
	[Arguments]         ${json_path}        ${value}                ${http_status_code}

	${fake_ehr_ref}=	Evaluate    str(uuid.uuid4())    uuid
						Set Test Variable    ${subject_id}    ${fake_ehr_ref}
	${payload}=    		generate payload from example json    ${json_path}    ${value}
						diagnostic.POST /Diagnostic  Diagnostic Report Radiology    ${payload}
						observation.validate response - 422 (w/o error message)    ${http_status_code}


generate payload from example json
	[Documentation]		Generates actual request payload using example json as a starting point.
	[Arguments]			${json_path}    ${value}

	${payload}          Load JSON From File    ${DATA_SET_PATH_DIAGNOSTIC}/RadiologyReport/create-radiology-report-normal-finding.json
                        Update Value To Json    ${payload}    $.subject.identifier.value    ${subject_id}
						Delete Object From Json    ${payload}    $.text

						# comment: delete field/object that has value 'missing' in test case table 
						Run Keyword And Return If   $value=="missing"
						...    	Run Keyword    Delete Object From Json    ${payload}    ${json_path}

						# comment: set value from data table in test case
						Update Value To Json            ${payload}    ${json_path}    ${value}
						Output Debug Info To Console    ${payload}

	[Return]			${payload}