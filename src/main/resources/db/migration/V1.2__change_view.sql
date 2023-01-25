DROP VIEW public.training_full;

CREATE OR REPLACE VIEW public.training_full
    AS
     SELECT participants.s_name,
    participants.f_name,
    trainers.s_name AS trainer_s_name,
    trainers.f_name AS trainer_f_name
   FROM training
     LEFT JOIN participants ON participants.participant_id = training.participant_id
     LEFT JOIN trainers ON trainers.trainer_id = training.trainer_id;

ALTER TABLE public.training_full
    OWNER TO postgres;