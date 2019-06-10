import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import warnings
import csv

#### Import csv file

data = pd.read_csv("recommendation_system\\ratings.data")

#### Setup the imported data
ratings = np.transpose(data.values)
R = np.copy(ratings)
#### Array where [i, j] i corresponds to a movie and j each users rating of the movie
#### This is a one-hot matrix where if the user has rated a movie, the value in the
#### corresponding index equals 1
R[R != 0] = 1

#### Convenient numbers to know
n_movies, n_users = ratings.shape

#### Hyperparameter, defines how many "classification" classes algorithm optimizes.
#### The more items and users there are, the higher this value should be.
n_features = 2

#### Initialize weight matrices using the variables set up above.
MovieFeatures = np.random.rand(n_movies, n_features)
UserPreferences = np.random.rand(n_users, n_features)

#### PARAMETERS >>
#   MovieFeatures   np.array of size ("number of movies", "number of feature classes")
#   UserPreferences np.array of size ("number of users", "number of feature classes")
#   ratings         np.array of imported data
#   lam             Float for regularization purposes
####
#### RETURNS >>
#   loss            Float of calculated loss for the current weights
#   UserPreferences_grad    np.array of the same size as UserPreferences,
#                           contains gradients for every User's preference weights
#   MovieFeatures_grad      np.array of the same size as MovieFeatures,
#                           contains gradients for every movies' feature weights
####
####
#### ////   Calculates loss, and gradients of the current weight values
####
def lossFunction(MovieFeatures, UserPreferences, ratings, lam):
    #### Calculates loss
    scores = np.square(np.dot(MovieFeatures, np.transpose(UserPreferences)) - ratings)
    loss = np.sum(scores[R == 1])/2 + ((np.sum(np.square(UserPreferences)) + np.sum(np.square(MovieFeatures)))/2*lam)

    #### Calculates gradients for both weight matrices
    scores = np.multiply(np.dot(MovieFeatures, np.transpose(UserPreferences)) - ratings, R)
    UserPreferences_grad = np.dot(np.transpose(scores), MovieFeatures) + lam*UserPreferences
    MovieFeatures_grad = np.dot(scores, UserPreferences) + lam*MovieFeatures

    return loss, UserPreferences_grad, MovieFeatures_grad


#### HYPERPARAMETERS >>
#### Number of training loops
loops = 500
#### Regularization hyperparamater, should be over 0
lam = 0
#### Learning rate alpha
alpha = 0.01

#### Initialize array to keep track if loss is decreasing or not
losses = []

#### Training loop, every loop calculates the gradients for MovieFeatures and
#### UserPreferences arrays and updates them with a learning rate of alpha
for i in range(loops):
    loss, UserPreferences_grad, MovieFeatures_grad = lossFunction(MovieFeatures, UserPreferences, ratings, lam)
    losses.append(loss)
    MovieFeatures = MovieFeatures - alpha * MovieFeatures_grad
    UserPreferences = UserPreferences - alpha * UserPreferences_grad


#### Calculate the predictions
scores = np.dot(MovieFeatures, np.transpose(UserPreferences))
#### We don't need predictions for movies that user has already rated
scores[R==1] = 0
scores = np.transpose(scores)

movieIds = data.columns.tolist()
userIds = data.index.tolist()
sortedScores = []
for i in range(len(scores)):
    zips = sorted(zip(scores[i, :], movieIds), reverse=True)
    userMovies = [userIds[i]]
    for j, k in zips:
        if (j > 0.0):
            userMovies.append(k)
        else:
            break
    sortedScores.append(userMovies)


#### Write to csv
with open('recommendation_system\\recommendations.csv', mode='w', newline='') as csv_file:
    writer = csv.writer(csv_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)

    #### Write a line for every user
    for i in sortedScores:
        writer.writerow(i)
