# Copyright (c) 2020 Peter Wohlfarth (Appsfactory GmbH), Wladislaw Wagner (Vitasystems GmbH), 
# Dave Petzold (Appsfactory GmbH)
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

Force Tags              create



*** Variables ***




*** Test Cases ***
001 Create Body Temperature 
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create body temperature    create-body-temp.json
    observation.validate response - 201


002 Create Blood Pressure 
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create blood pressure    create-blood-pressure.json
    observation.validate response - 201


003 Create FIO2 
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create FIO2    create-fio2.json
    observation.validate response - 201


004 Create Heart Rate 
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create heart rate    create-heart-rate.json
    observation.validate response - 201


005 Create Sofa Score
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create sofa score    create-sofa-score.json
    observation.validate response - 201


006 Create Observation Lab
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create observation lab    create-observation-lab.json
    observation.validate response - 201


007 Create Observation Using Default Profile
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create observation    create-with-default-profile.json
    observation.validate response - 422 (default profile not supported)


008 Create Observation Using Unsupported Profile
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create observation    create-with-unsupported-profile.json
    observation.validate response - 422 (profile not supported)


009 Create Coronavirus Lab Result
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create observation    create-coronavirus-nachweis-test.json
    observation.validate response - 201


010 Create Body Height
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create observation  create-body-height.json
	observation.validate response - 201


011 Create Pregnancy Status
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create pregnancy status    create-pregnancy-status.json
  	observation.validate response - 201


012 Create Frailty Scale Score
	[Documentation]    1. create EHR
	...                2. trigger observation endpoint

	ehr.create new ehr    000_ehr_status.json
	observation.create frailty scale score    create-clinical-frailty-scale-score.json
  	observation.validate response - 201
