package com.reagroup.appliedscala.urls.savereview

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.effect.IO
import cats.implicits._
import com.reagroup.appliedscala.models.{Movie, MovieId}

class SaveReviewService(saveReview: (MovieId, ValidatedReview) => IO[ReviewId],
                        fetchMovie: MovieId => IO[Option[Movie]]) {

  /**
    * Before saving a `NewReviewRequest`, we want to check that the movie exists and then
    * validate the request in order to get a `ValidatedReview`.
    * Complete `NewReviewValidator`, then use it here before calling `saveReview`.
    * Return all errors encountered whether the movie exists or not.
    *
    */
  def save(movieId: MovieId, review: NewReviewRequest): IO[ValidatedNel[ReviewValidationError, ReviewId]] = {
    fetchMovie(movieId).flatMap { maybeMovie: Option[Movie] =>
      val movieValidation: ValidatedNel[ReviewValidationError, Movie]            = validateMovie(maybeMovie)
      val reviewValidation: ValidatedNel[ReviewValidationError, ValidatedReview] = validateReview(review)
      val oneBigValidation: ValidatedNel[ReviewValidationError, ValidatedReview] = movieValidation.productR(reviewValidation)
//        (movieValidation, reviewValidation).mapN((_, validatedReview) => validatedReview)

      oneBigValidation.traverse(validatedReview => saveReview(movieId, validatedReview))
    }
  }

  private def validateMovie(maybeMovie: Option[Movie]): ValidatedNel[ReviewValidationError, Movie] = {
    maybeMovie match {
      case Some(movie) => movie.validNel
      case None => MovieDoesNotExist.invalidNel
    }
  }

  private def validateReview(review: NewReviewRequest): ValidatedNel[ReviewValidationError, ValidatedReview] = {
    NewReviewValidator.validate(review)
  }

  // Option[Person] - maybePerson / optPerson
  // IO[Person] - fetchPerson / getPerson
  // IO[ReviewId] - saveReview
  // Either[Error, Person] - errorOrPerson
  // ValidatedNel[Error, Person] - errorsOrPerson / validatePerson.map(validatedPerson => ???)

  // Option[Person] | person.map(p => p.name) / maybePerson.map(person => person.name)

}
