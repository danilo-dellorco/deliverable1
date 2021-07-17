# ISW2 - Deliverable1 [![Build Status](https://travis-ci.com/danilo-dellorco/deliverable1.svg?branch=master)](https://travis-ci.com/danilo-dellorco/deliverable1) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=danilo-dellorco_deliverable1&metric=code_smells)](https://sonarcloud.io/dashboard?id=danilo-dellorco_deliverable1)
Codice relativo al primo Deliverable. Effettua l'analisi su ticket e commit di tipo bug fix relativi ad un progetto, fornendo in output i dati necessari per realizzare un Process Control Chart

# Configurazione
1. Specificare nel parametro ```JIRA_PROJECT_NAME``` il nome del progetto come riportato su JIRA
2. Specificare nel parametro  ```GIT_PROJECT_NAME``` il nome del progetto come riportato su GitHub

# Manuale
1. Lanciare ```src/main/java/logic/AnalyzeProject.java``` per genera
2. Lanciare ```src/main/java/dataset/AnalyzeDataset.java``` per analizzare i dataset tramite i classificatori
Il report con il numero di bug fixed per ogni mese generato nella home directory nel file  ```PROJECTNAME_DATA.csv```
