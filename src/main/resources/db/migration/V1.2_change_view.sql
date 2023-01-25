CREATE OR REPLACE VIEW public.trainers_full
    AS
 SELECT trainers.trainer_id,
    trainers.s_name,
    trainers.birth_date,
    trainers.f_name
   FROM trainers;
ALTER TABLE public.training_full
    OWNER TO postgres;