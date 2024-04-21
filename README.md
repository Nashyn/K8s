# Kubernetes Project (K8s-main)

Welcome to the `K8s-main` repository, a project demonstrating the use of Kubernetes and Terraform to deploy and manage containerized applications. This project includes scripts and configuration files for setting up a Kubernetes environment, deploying applications, and managing persistent storage.

## Features

- **Application Deployment**: Easy deployment scripts for containerized applications using Kubernetes.
- **Persistent Storage**: Scripts for setting up and managing persistent volumes in Kubernetes.
- **Infrastructure as Code**: Utilize Terraform to provision and manage Kubernetes clusters and associated resources.

## Prerequisites

Before you begin, ensure you have the following installed:
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/), the Kubernetes command-line tool
- [Terraform](https://www.terraform.io/downloads.html), for infrastructure provisioning
- Access to a Kubernetes cluster

## Installation

### Kubernetes Resources

1. To create persistent volumes, run the provided shell script:
   ```
   ./create-persistent-volume.sh
   ```

2. Apply the Kubernetes configurations to set up the persistent volume claims:
```
kubectl apply -f pv-claim.yaml
```

###Terraform Setup
Initialize Terraform to download the necessary providers:
````
terraform init
````
Apply the Terraform configuration to set up the Kubernetes resources:
```
terraform apply
```

###Deploy Applications
Navigate to the application directories (calculator, validator) and deploy each using:
```
kubectl apply -f <application-deployment-file>
```

## Usage
Once the applications are deployed, they can be accessed via their respective services exposed on the Kubernetes cluster. Manage resources using kubectl commands to scale, update, and monitor the applications.
