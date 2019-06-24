### By Miikael
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import warnings
import firebase_admin
from firebase_admin import db
import json


## Initialize Firebase and the variables

app = firebase_admin.initialize_app()
likeList = db.reference('/Discovery', app, "https://flixtyle.firebaseio.com").get()
itemList = db.reference('/items', app, "https://flixtyle.firebaseio.com").get()
userRef = db.reference('/Users', app, "https://flixtyle.firebaseio.com")
userList = list(userRef.get().keys())

itemList = list(itemList['topman'].keys()) + list(itemList['zara'].keys())


## Set up the ratings array
ratings = np.zeros((len(userList), len(itemList)))

for i in likeList:
    userIndex = userList.index(i)
    for k in likeList[i]:
        itemIndex = itemList.index(k)
        ratings[userIndex][itemIndex] = 1 if likeList[i][k] == True else -1


#### Setup the imported data
ratings = ratings.transpose()
R = np.copy(ratings)
#### Array where [i, j] i corresponds to a item and j each users rating of the item
#### This is a one-hot matrix where if the user has rated a item, the value in the
#### corresponding index equals 1
R[R != 0] = 1

#### Convenient numbers to know
n_items, n_users = ratings.shape

#### Hyperparameter, defines how many "classification" classes algorithm optimizes.
#### The more items and users there are, the higher this value should be.
n_features = 2

#### Initialize weight matrices using the variables set up above.
itemFeatures = np.random.rand(n_items, n_features)
userPreferences = np.random.rand(n_users, n_features)

#### PARAMETERS >>
#   itemFeatures   np.array of size ("number of items", "number of feature classes")
#   userPreferences np.array of size ("number of users", "number of feature classes")
#   ratings         np.array of imported data
#   lam             Float for regularization purposes
####
#### RETURNS >>
#   loss            Float of calculated loss for the current weights
#   userPreferences_grad    np.array of the same size as userPreferences,
#                           contains gradients for every user's preference weights
#   itemFeatures_grad      np.array of the same size as itemFeatures,
#                           contains gradients for every items' feature weights
####
####
#### ////   Calculates loss, and gradients of the current weight values
####
def lossFunction(itemFeatures, userPreferences, ratings, lam):
    #### Calculates loss
    scores = np.square(np.dot(itemFeatures, np.transpose(userPreferences)) - ratings)
    loss = np.sum(scores[R == 1])/2 + ((np.sum(np.square(userPreferences)) + np.sum(np.square(itemFeatures)))/2*lam)

    #### Calculates gradients for both weight matrices
    scores = np.multiply(np.dot(itemFeatures, np.transpose(userPreferences)) - ratings, R)
    userPreferences_grad = np.dot(np.transpose(scores), itemFeatures) + lam*userPreferences
    itemFeatures_grad = np.dot(scores, userPreferences) + lam*itemFeatures

    return loss, userPreferences_grad, itemFeatures_grad


#### HYPERPARAMETERS >>
#### Number of training loops
loops = 500
#### Regularization hyperparamater, should be over 0
lam = 0
#### Learning rate alpha
alpha = 0.01

#### Initialize array to keep track if loss is decreasing or not
losses = []

#### Training loop, every loop calculates the gradients for itemFeatures and
#### userPreferences arrays and updates them with a learning rate of alpha
for i in range(loops):
    loss, userPreferences_grad, itemFeatures_grad = lossFunction(itemFeatures, userPreferences, ratings, lam)
    losses.append(loss)
    itemFeatures = itemFeatures - alpha * itemFeatures_grad
    userPreferences = userPreferences - alpha * userPreferences_grad


#### Calculate the predictions
scores = np.dot(itemFeatures, np.transpose(userPreferences))
#### We don't need predictions for items that user has already rated
scores[R==1] = 0
scores = np.transpose(scores)


itemIds = itemList
userIds = userList
sortedScores = []
index = 0
### This sorts the scores for every user, with the highest scores first, and
### Saves the recommended items' ID to the database for every user under recommendations property
for i in range(len(scores)):
    zips = sorted(zip(scores[i, :], itemIds), reverse=True)
    userItems = []
    for j, k in zips:
        index += 1
        ## 50 is the amount of recommendations saved in the system
        if index > 50:
            index = 0
            userRef.child(userIds[i]).update({"recommendations": userItems})
            break
        userItems.append(k)
    sortedScores.append(userItems)