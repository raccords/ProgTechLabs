ALTER TABLE IF EXISTS public.results
    ADD COLUMN secondary_score integer;

CREATE OR REPLACE VIEW public.results_full
    AS
     SELECT results.date,
    participants.f_name,
    participants.s_name,
    results.participant_id,
    results.score,
	results.secondary_score
   FROM results
     LEFT JOIN participants ON participants.participant_id = results.participant_id;