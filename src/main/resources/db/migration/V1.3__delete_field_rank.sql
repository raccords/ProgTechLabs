DROP VIEW public.trainers_full;

CREATE OR REPLACE VIEW public.trainers_full
    AS
     SELECT trainers.trainer_id,
    trainers.s_name,
    trainers.birth_date,
    trainers.f_name
   FROM trainers;

ALTER TABLE public.trainers_full
    OWNER TO postgres;

ALTER TABLE public.trainers
 DROP COLUMN rank CASCADE;