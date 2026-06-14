export interface IQuiz {
  subtopicId: number;
  questionText: string;
  options: QuizOption[];
}

export interface QuizOption {
  id: number;
  optionText: string;
}
